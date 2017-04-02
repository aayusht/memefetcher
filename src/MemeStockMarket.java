import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jiggereepuff on 4/1/2017.
 */
public class MemeStockMarket {
    public ArrayList<MemeStock> stocks;
    public HashMap<String, MemeStock> nameFinder;
    public HashMap<String, MemeStock> abbrevFinder;
    public static MemeStockMarket GLOBAL = new MemeStockMarket(null);

    public MemeStockMarket(ArrayList<MemeStock> stocks) {
        this.stocks = stocks;
        for (MemeStock ms : stocks) {
            nameFinder.put(ms.getMeme().getName(), ms);
            abbrevFinder.put(ms.getMeme().getAbbrev(), ms);
        }
    }

    private void passADay() {
        for (MemeStock ms : stocks) {
            ms.getMarketValue();
        }
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
