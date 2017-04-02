package com.memeteam.lahacks.spice;

import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hp on 4/1/2017.
 */

public class Meme implements Comparable<Meme>{
    String name;
    String abbrev = "";
    static final double EPSILON = .001;
    ArrayList<Pair<Date, Integer>> scores;
    double value;

    public Meme(String name, ArrayList<Pair<Date, Integer>> scores) {
        this.name = name;
        this.scores = scores;
        this.value = (new MemeStock(this)).getMarketValue();
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) != ' ') {
                abbrev += Character.toUpperCase(name.charAt(i));
                counter += 1;
            }
            if (counter == 3) {
                i = name.length();
            }
        }
    }

    @Override
    public int compareTo(@NonNull Meme o) {
        return Math.abs(value - o.value) < EPSILON ? 0 : (int) (1000 * value - 1000 * o.value);
    }
}
