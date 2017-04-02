import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Jiggereepuff on 4/1/2017.
 */
public class MemeStockMarket {
    public ArrayList<MemeStock> stocks;
    public HashMap<String, MemeStock> nameFinder;
    public HashMap<String, MemeStock> abbrevFinder;
    public ArrayList<DatePair> totalMarketHist;
    public static MemeStockMarket GLOBAL;

    public MemeStockMarket(ArrayList<MemeStock> stocks) {
        this.stocks = stocks;
        nameFinder = new HashMap<>();
        abbrevFinder = new HashMap<>();
        for (MemeStock ms : stocks) {
            nameFinder.put(ms.getMeme().getName(), ms);
            abbrevFinder.put(ms.getMeme().getAbbrev(), ms);
        }
        totalMarketHist = new ArrayList<>();
        double total = 0;
        for (MemeStock ms : stocks) {
            total += ms.peekCurrValue();
        }
        totalMarketHist.add(new DatePair(total, stocks.get(0).peekCurrDate()));
    }

    private void passADay() {
        Date nextWeek = stocks.get(0).peekCurrDate();
        double total = 0;
        for (MemeStock ms : stocks) {
            ms.getMarketValue();
            total += ms.peekCurrValue();
            nextWeek = new Date(ms.peekCurrDate().getTime() + 7 * 24 * 60 * 60 * 1000);
        }
        totalMarketHist.add(new DatePair(total, nextWeek));
    }

    public void fullUpdate() {
        Date latestTime = stocks.get(0).peekCurrDate();
        while (latestTime.getTime() > totalMarketHist.get(totalMarketHist.size() - 1).getDate().getTime()) {
            passADay();
        }
    }

    public static void makeGlobal(ArrayList<Meme> allInput) {
        ArrayList<MemeStock> input2 = new ArrayList<>();
        for (Meme m : allInput) {
            input2.add(new MemeStock(m));
        }
        GLOBAL = new MemeStockMarket(input2);
    }

    public ArrayList<MemeStock> mostPopularStocks(int num) {
        int maxIndex, counter;
        ArrayList<MemeStock> dankest = new ArrayList<>();
        ArrayList<MemeStock> copy = new ArrayList<>();
        for (MemeStock m : stocks) {
            copy.add(m);
        }
        for (int i = 0; i < num; i ++) {
            maxIndex = 0;
            counter = 0;
            for (MemeStock ms : dankest) {
                if (ms.peekCurrValue() > copy.get(counter).peekCurrValue()) {
                    maxIndex = counter;
                }
                counter += 1;
            }
            dankest.add(copy.get(maxIndex));
            copy.remove(maxIndex);
        }
        return dankest;
    }
}
