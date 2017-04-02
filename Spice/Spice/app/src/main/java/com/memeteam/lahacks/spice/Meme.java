package com.memeteam.lahacks.spice;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hp on 4/1/2017.
 */

public class Meme {
    String name;
    ArrayList<Pair<Date, Integer>> scores;
    int index;
    public Meme(String name, ArrayList<Pair<Date, Integer>> scores) {
        this.name = name;
        this.scores = scores;
        //TODO compute index from scores
    }
}
