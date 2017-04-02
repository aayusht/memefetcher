package com.memeteam.lahacks.spice;

import android.util.Pair;

import com.google.android.gms.common.stats.StatsEvent;

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
    private ArrayList<Pair<Date, Integer>> priceHistory;

    /*
    Make sure to call predict before you get the next price
     */

    public MemeStock(Meme m) {
        base = m;
        priceHistory = new ArrayList<>();
        stockPrice = getMarketValue();
    }

    public double getPredMarketValue() {
        List<Pair<Date, Integer>> ratings = base.scores;
        if (priceHistory.size() < 2) {
            throw new UnsupportedOperationException("Too little data");
        }
        double scale1;
        double trend = priceHistory.get(priceHistory.size() - 1).second
                - priceHistory.get(priceHistory.size() - 2).second;
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
        volatility = stddev(copy);
        /*
        check if volatility more than normal, if more volatile less weight on first one
         */
        scale1 = (1.0 * ratings.size() / 5.0) / volatility;
        return roundToNearestCent(scale1 * priceHistory.get(priceHistory.size() - 1).second
                + (1 - scale1) * getAverage(priceHistory) + trend / (4 * scale1));
    }
    
    private static double stddev(int[] arr) {
        double sum = 0;
        for (int i : arr) {
            sum += i;
        }
        return Math.sqrt(sum);
    }

    public double getMarketValue() {
        double a = peekCurrValue();
        double b;
        if (priceHistory.size() < 2) {
            b = a;
        } else {
            b = base.scores.get(base.scores.size() - 2).second;
        }
        double center = (a + b) / 4 + 20;
        a = center + (a - b) / 2;
        b = center + (b - a) / 2;
        stockPrice = roundToNearestCent(2 * a - b);
        priceHistory.add(new Pair<Date, Integer>(new Date(
                getPriceHistory().get(priceHistory.size() - 1).first.getTime() + 7 * 24 * 60 * 60 * 1000), (int) stockPrice));
        return stockPrice;
    }

    public List<Pair<Date, Integer>> getPriceHistory() {
        return priceHistory;
    }

    public int getIndexMax() {
        int maxIndex = 0;
        for (int i = 1; i < base.scores.size(); i++) {
            if (base.scores.get(i).second > base.scores.get(maxIndex).second) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public int getIndexMin() {
        int minIndex = 0;
        for (int i = 1; i < base.scores.size(); i++) {
            if (base.scores.get(i).second < base.scores.get(minIndex).second) {
                minIndex = i;
            }
        }
        return minIndex;
    }

    public double getAverage(List<Pair<Date, Integer>> data) {
        double total = 0;
        for (Pair<Date, Integer> d : data) {
            total += d.second;
        }
        return total / data.size();
    }

    /* Called after getMarketValue()
    First Number is Total change, second is percent change
     */
    public double[] getChange() {
        double netChange = getPriceHistory().get(getPriceHistory().size() - 1).second
                - getPriceHistory().get(getPriceHistory().size() - 2).second;
        double percentage = netChange / peekCurrValue();
        double[] changes = new double[2];
        changes[0] = netChange;
        changes[1] = percentage;
        return changes;
    }

    public double peekCurrValue() {
        return getPriceHistory().get(Math.max(getPriceHistory().size() - 1, 0)).second;
    }

    public Date peekCurrDate() { return getPriceHistory().get(Math.max(getPriceHistory().size() - 1, 0)).first; }

    public Meme getMeme() {
        return base;
    }

    public static double roundToNearestCent(double d) {
        return (int) (d * 100) / 100.0;
    }

}
