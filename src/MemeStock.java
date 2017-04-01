import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;

/**
 * Created by Jiggereepuff on 4/1/2017.
 */
public class MemeStock {
    private Meme base;
    private double stockPrice;
    private double volatility;
    private ArrayList<Double> priceHistory;
    private ArrayList<Double> volatilityHistory;
    private boolean boughtPremium = false;

    public MemeStock(Meme m) {
        base = m;
        priceHistory = new ArrayList<>();
        volatilityHistory = new ArrayList<>();
        stockPrice = getMarketValue();
    }

    public MemeStock(Meme m, double startingPrice) {
        base = m;
        stockPrice = startingPrice;
        priceHistory = new ArrayList<>();
        volatilityHistory = new ArrayList<>();
        priceHistory.add(startingPrice);
    }

    public double getPredMarketValue() {
        List<Integer> ratings = base.getRatings();
        if (priceHistory.size() < 2) {
            return 750;
        }
        double scale1;
        double trend = priceHistory.get(priceHistory.size() - 1) - priceHistory.get(priceHistory.size() - 2);
        List<Integer> ratingsCopy = new ArrayList<>();
        int high = getIndexMax();
        int low = getIndexMin();
        for (int i = 0; i < ratings.size(); i++) {
            if (i != high && i != low) {
                ratingsCopy.add(i);
            }
        }
        int[] copy = new int[ratingsCopy.size()];
        int counter = 0;
        for (int i : ratingsCopy) {
            copy[counter] = i;
            counter += 1;
        }
        volatility = StdStats.stddev(copy);
        /*
        check if volatility more than normal, if more volatile less weight on first one
         */
        volatilityHistory.add(volatility);
        scale1 = (1.0 * ratings.size() / 5.0) / volatility;
        return scale1 * priceHistory.get(priceHistory.size() - 1) +
                (1 - scale1) * getAverage(priceHistory) + trend / (4 * scale1);
    }

    public double getMarketValue() {
        if (priceHistory.size() == base.getRatings().size()) {
            return priceHistory.get(priceHistory.size() - 1);
        }
        double a = base.getRatings().get(priceHistory.size());
        double b;
        if (priceHistory.size() < 2) {
            b = a;
        } else {
            b = base.getRatings().get(base.getRatings().size() - 2);
        }
        double center = (a + b) / 4 + 20;
        a = center + (a - b) / 2;
        b = center + (b - a) / 2;
        stockPrice = 2 * a - b;
        priceHistory.add(stockPrice);
        return stockPrice;
    }

    public int getIndexMax() {
        int maxIndex = 0;
        for (int i = 1; i < base.getRatings().size(); i++) {
            if (base.getRatings().get(i) > base.getRatings().get(maxIndex)) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public int getIndexMin() {
        int minIndex = 0;
        for (int i = 1; i < base.getRatings().size(); i++) {
            if (base.getRatings().get(i) > base.getRatings().get(minIndex)) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    public double getAverage(List<Double> data) {
        double total = 0;
        for (double i : data) {
            total += i;
        }
        return total / data.size();
    }

    public void buyPremium() {
        boughtPremium = true;
    }

    public void visualizePremium() {
        if (!boughtPremium) {
            throw new MissingFormatArgumentException("Please buy!");
        }
        System.out.println("We expect your meme to be " + getPredMarketValue() + " in the next time step");
    }

    public static void main(String[] args) {
        ArrayList<Integer> ratings = new ArrayList<>();
        ratings.add(11);
        ratings.add(18);
        ratings.add(15);
        ratings.add(15);
        ratings.add(17);
        ratings.add(15);
        ratings.add(12);
        ratings.add(29);
        ratings.add(35);
        ratings.add(32);
        ratings.add(29);
        ratings.add(29);
        ratings.add(26);
        ratings.add(25);
        ratings.add(23);
        ratings.add(25);
        ratings.add(22);
        ratings.add(18);
        ratings.add(18);
        ratings.add(17);
        ratings.add(18);
        ratings.add(17);
        ratings.add(18);
        ratings.add(25);
        ratings.add(23);
        ratings.add(20);
        ratings.add(20);
        Meme m = new Meme("Bad luck Brian", ratings, "hi");
        MemeStock s = new MemeStock(m);
        for (int i = 0; i < ratings.size(); i ++) {
            System.out.println("Predicted: " + s.getPredMarketValue() + ", Actual: " + s.getMarketValue());
        }
    }
}
