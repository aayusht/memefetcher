package com.memeteam.lahacks.spice;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Jiggereepuff on 4/1/2017.
 */
public class MemeStockMarket {
    public ArrayList<MemeStock> stocks;
    public HashMap<String, MemeStock> nameFinder;
    public ArrayList<Pair<Date, Integer>> totalMarketHist;
    public static MemeStockMarket GLOBAL = new MemeStockMarket(null);

    public MemeStockMarket(ArrayList<MemeStock> stocks) {
        this.stocks = stocks;
        for (MemeStock ms : stocks) {
            nameFinder.put(ms.getMeme().name, ms);
        }
        totalMarketHist = new ArrayList<>();
        double total = 0;
        for (MemeStock ms : stocks) {
            total += ms.peekCurrValue();
        }
        totalMarketHist.add(new Pair<Date, Integer>(stocks.get(0).peekCurrDate(), (int) total));
    }

    private void passADay() {
        Date nextWeek = stocks.get(0).peekCurrDate();
        double total = 0;
        for (MemeStock ms : stocks) {
            ms.getMarketValue();
            total += ms.peekCurrValue();
            nextWeek = new Date(ms.peekCurrDate().getTime() + 7 * 24 * 60 * 60 * 1000);
        }
        totalMarketHist.add(new Pair<Date, Integer>(nextWeek, (int) total));
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
