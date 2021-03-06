package com.memeteam.lahacks.spice;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.MissingFormatArgumentException;

/**
 * Created by Jiggereepuff on 4/1/2017.
 */
public class MyMemeAcc {
    private boolean boughtPremium;
    private double accBalance;
    private double stockEquity;
    private HashMap<MemeStock, Integer> ownedStock;
    private ArrayList<Pair<Date, Integer>> totalEquityHist;

    public MyMemeAcc(Date start) {
        boughtPremium = false;
        accBalance = 0;
        ownedStock = new HashMap<>();
        stockEquity = 0;
        totalEquityHist = new ArrayList<>();
        totalEquityHist.add(new Pair<Date, Integer>(start, 0));
    }

    public MyMemeAcc(int initBalance, Date start) {
        boughtPremium = false;
        accBalance = MemeStock.roundToNearestCent(initBalance);
        ownedStock = new HashMap<>();
        stockEquity = 0;
        totalEquityHist = new ArrayList<>();
        totalEquityHist.add(new Pair<Date, Integer>(start, initBalance));
    }

//    public void purchaseStock(String name, int quantity) {
//        MemeStock bought;
//        if (name.length() == 3) {
//            //bought = MemeStockMarket.GLOBAL.abbrevFinder.get(name);
//        } else {
//            bought = MemeStockMarket.GLOBAL.nameFinder.get(name);
//        }
//        if (bought == null) {
//            throw new IllegalArgumentException("No meme with name found");
//        }
//        ownedStock.putIfAbsent(bought, 0);
//        if (accBalance < quantity * bought.peekCurrValue()) {
//            throw new IndexOutOfBoundsException("Not enough money!");
//        }
//        accBalance -= quantity * bought.peekCurrValue();
//        stockEquity += quantity * bought.peekCurrValue();
//        ownedStock.put(bought, ownedStock.get(bought) + quantity);
//    }

//    public void sellStock(String name, int quantity) {
//        MemeStock sold;
//        if (name.length() == 3) {
//            sold = MemeStockMarket.GLOBAL.abbrevFinder.get(name);
//        } else {
//            sold = MemeStockMarket.GLOBAL.nameFinder.get(name);
//        }
//        if (sold == null) {
//            throw new IllegalArgumentException("No meme with name found");
//        }
//        Integer inventory = ownedStock.get(sold);
//        if (inventory == null || inventory < quantity) {
//            throw new IndexOutOfBoundsException("Not enough of that stock to be sold");
//        }
//        accBalance += quantity * sold.peekCurrValue();
//        stockEquity -= quantity * sold.peekCurrValue();
//        ownedStock.put(sold, ownedStock.get(sold) - quantity);
//    }

    public double checkBalance() {
        return accBalance;
    }

    public double checkTotalEquity() {
        return accBalance + stockEquity;
    }

    public void buyPremium() {
        if (accBalance < 1000) {
            throw new IndexOutOfBoundsException("Not enough money!");
        }
        accBalance -= 1000;
        boughtPremium = true;
    }

    public ArrayList<Pair<Date, Integer>> getTotalEquityHist() {
        return totalEquityHist;
    }

    public void visualizePremium() {
        if (!boughtPremium) {
            throw new MissingFormatArgumentException("Please buy!");
        }
        System.out.println("We expect your meme to be " + " in the next time step");
    }
}
