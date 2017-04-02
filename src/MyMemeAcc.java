import java.util.*;

/**
 * Created by Jiggereepuff on 4/1/2017.
 */
public class MyMemeAcc {
    private boolean boughtPremium;
    private double accBalance;
    private double stockEquity;
    private MemeStockMarket myStockMarket;
    private HashMap<MemeStock, Integer> ownedStock;
    private ArrayList<DatePair> totalEquityHist;
    private ArrayList<MemeStock> diffStocks;

    public MyMemeAcc(Date start) {
        boughtPremium = false;
        accBalance = 0;
        ownedStock = new HashMap<>();
        stockEquity = 0;
        totalEquityHist = new ArrayList<>();
        totalEquityHist.add(new DatePair(0, start));
        Set<MemeStock> hold = ownedStock.keySet();
        diffStocks = new ArrayList<>();
        for (MemeStock ms : hold) {
            diffStocks.add(ms);
        }
        myStockMarket = new MemeStockMarket(diffStocks);
    }

    public MyMemeAcc(int initBalance, Date start) {
        boughtPremium = false;
        accBalance = MemeStock.roundToNearestCent(initBalance);
        ownedStock = new HashMap<>();
        stockEquity = 0;
        totalEquityHist = new ArrayList<>();
        totalEquityHist.add(new DatePair(initBalance, start));
        Set<MemeStock> hold = ownedStock.keySet();
        diffStocks = new ArrayList<>();
        for (MemeStock ms : hold) {
            diffStocks.add(ms);
        }
        myStockMarket = new MemeStockMarket(diffStocks);
    }

    public void purchaseStock(String name, int quantity) {
        MemeStock bought;
        if (name.length() == 3) {
            bought = MemeStockMarket.GLOBAL.abbrevFinder.get(name);
        } else {
            bought = MemeStockMarket.GLOBAL.nameFinder.get(name);
        }
        if (bought == null) {
            throw new IllegalArgumentException("No meme with name found");
        }
        ownedStock.putIfAbsent(bought, 0);
        if (accBalance < quantity * bought.peekCurrValue()) {
            throw new IndexOutOfBoundsException("Not enough money!");
        }
        accBalance -= quantity * bought.peekCurrValue();
        stockEquity += quantity * bought.peekCurrValue();
        ownedStock.put(bought, ownedStock.get(bought) + quantity);
    }

    public void sellStock(String name, int quantity) {
        MemeStock sold;
        if (name.length() == 3) {
            sold = MemeStockMarket.GLOBAL.abbrevFinder.get(name);
        } else {
            sold = MemeStockMarket.GLOBAL.nameFinder.get(name);
        }
        if (sold == null) {
            throw new IllegalArgumentException("No meme with name found");
        }
        Integer inventory = ownedStock.get(sold);
        if (inventory == null || inventory < quantity) {
            throw new IndexOutOfBoundsException("Not enough of that stock to be sold");
        }
        accBalance += quantity * sold.peekCurrValue();
        stockEquity -= quantity * sold.peekCurrValue();
        ownedStock.put(sold, ownedStock.get(sold) - quantity);
    }

    public double checkBalance() {
        return accBalance;
    }

    public double checkTotalEquity() {
        updateEquity();
        return accBalance + stockEquity;
    }

    public void buyPremium() {
        if (accBalance < 1000) {
            throw new IndexOutOfBoundsException("Not enough money!");
        }
        accBalance -= 1000;
        boughtPremium = true;
    }

    public ArrayList<DatePair> getTotalEquityHist() {
        return totalEquityHist;
    }

    public void updateEquity() {
        myStockMarket.fullUpdate();
        double tempEquity = 0;
        for (MemeStock ms : diffStocks) {
            tempEquity += ownedStock.get(ms) * ms.peekCurrValue();
        }
        stockEquity = tempEquity;
    }

    public void visualizePremium() {
        if (!boughtPremium) {
            throw new MissingFormatArgumentException("Please buy!");
        }
        System.out.println("Our predictor has predicted the following gains/losses for your stocks");
        for (MemeStock ms : diffStocks) {
            System.out.println("Predicted gain/loss for meme " + ms.getMeme().getName() + ": " + ms.getChange()[0]
                    + " (" + ms.getChange()[1] + "). ");
        }
    }

    public static void main(String[] args) {
        ArrayList<DatePair> a = new ArrayList<>();
        long start = 0;
        for (int i = 0; i < 300; i ++) {
            a.add(new DatePair(i * 2, new Date(start)));
            start += 7 * 24 * 60 * 60 * 1000;
        }
    }
}
