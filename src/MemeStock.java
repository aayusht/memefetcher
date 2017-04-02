import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.MissingFormatArgumentException;

/**
 * Created by Jiggereepuff on 4/1/2017.
 */
public class MemeStock {
    private Meme base;
    private double stockPrice;
    private double volatility;
    private ArrayList<DatePair> priceHistory;

    /*
    Make sure to call predict before you get the next price
     */

    public MemeStock(Meme m) {
        base = m;
        priceHistory = new ArrayList<>();
        stockPrice = getMarketValue();
    }

    public double getPredMarketValue() {
        List<DatePair> ratings = base.getRatings();
        if (priceHistory.size() < 2) {
            throw new UnsupportedOperationException("Too little data");
        }
        double scale1;
        double trend = priceHistory.get(priceHistory.size() - 1).getScore()
                - priceHistory.get(priceHistory.size() - 2).getScore();
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
        scale1 = (1.0 * ratings.size() / 5.0) / volatility;
        return roundToNearestCent(scale1 * priceHistory.get(priceHistory.size() - 1).getScore()
                + (1 - scale1) * getAverage(priceHistory) + trend / (4 * scale1));
    }

    public double getMarketValue() {
        double a = peekCurrValue();
        double b;
        if (priceHistory.size() < 2) {
            b = a;
        } else {
            b = base.getRatings().get(base.getRatings().size() - 2).getScore();
        }
        double center = (a + b) / 4 + 20;
        a = center + (a - b) / 2;
        b = center + (b - a) / 2;
        stockPrice = roundToNearestCent(2 * a - b);
        priceHistory.add(new DatePair(stockPrice, new Date(
                getPriceHistory().get(priceHistory.size() - 1).getDate().getTime() + 7 * 24 * 60 * 60 * 1000)));
        return stockPrice;
    }

    public List<DatePair> getPriceHistory() {
        return priceHistory;
    }

    public int getIndexMax() {
        int maxIndex = 0;
        for (int i = 1; i < base.getRatings().size(); i++) {
            if (base.getRatings().get(i).getScore() > base.getRatings().get(maxIndex).getScore()) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public int getIndexMin() {
        int minIndex = 0;
        for (int i = 1; i < base.getRatings().size(); i++) {
            if (base.getRatings().get(i).getScore() < base.getRatings().get(minIndex).getScore()) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    public double getAverage(List<DatePair> data) {
        double total = 0;
        for (DatePair d : data) {
            total += d.getScore();
        }
        return total / data.size();
    }

    /* Called after getMarketValue()
    First Number is Total change, second is percent change
     */
    public double[] getChange() {
        double netChange = getPriceHistory().get(getPriceHistory().size() - 1).getScore()
                - getPriceHistory().get(getPriceHistory().size() - 2).getScore();
        double percentage = netChange / peekCurrValue();
        double[] changes = new double[2];
        changes[0] = netChange;
        changes[1] = percentage;
        return changes;
    }

    public double peekCurrValue() {
        return getPriceHistory().get(getPriceHistory().size() - 1).getScore();
    }

    public Date peekCurrDate() { return getPriceHistory().get(getPriceHistory().size() - 1).getDate(); }

    public Meme getMeme() {
        return base;
    }

    public static double roundToNearestCent(double d) {
        return (int) (d * 100) / 100.0;
    }

}
