package com.memeteam.lahacks.spice;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = (TextView) findViewById(R.id.marquee);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView .setSingleLine(true);
        textView.setMarqueeRepeatLimit(-1);
        textView.setFocusableInTouchMode(true);
        textView.setFocusable(true);

        final ArrayList<Meme> memes = new ArrayList<>();
        final ListAdapter listAdapter = new ListAdapter(memes);
        class Shit extends AsyncTask<ArrayList<Meme>,Void,String> {
            @Override
            protected String doInBackground(ArrayList<Meme> ... memes) {
                MemeStockMarket.makeGlobal(memes[0]);
                MemeStockMarket.GLOBAL.fullUpdate();
                double diff = 0;
                boolean greater = true;
                MemeStock extreme = null;
                for (MemeStock ms : MemeStockMarket.GLOBAL.stocks) {
                    double d1 = ms.getPriceHistory().get(ms.getPriceHistory().size() - 2).second;
                    double d2 = ms.getPriceHistory().get(ms.getPriceHistory().size() - 1).second;
                    if (d1 > d2 && d1 - d2 > diff) {
                        diff = d1 - d2;
                        greater = false;
                        extreme = ms;
                    } else if (d2 > d1 && d2 - d1 > diff) {
                        diff = d2 - d1;
                        greater = true;
                        extreme = ms;
                    }
                }
                String marq = String.format("NASDANQ UP %d POINTS--S&MEME 500 DOWN %d POINTS--", (int) (Math.random() * 10), (int) (Math.random() * 10))
                        + extreme.getMeme().abbrev;
                if (greater) {
                    marq += " UP ";
                } else {
                    marq += " DOWN ";
                }
                marq += diff + " POINTS--";
                return marq;
            }
            protected void onPostExecute(String marq) {
                textView.setText(marq);
            }
        }
        final Shit s = new Shit();

        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                memes.clear();
                for (DataSnapshot name : dataSnapshot.getChildren()) {
                    ArrayList<Pair<Date, Integer>> pairArrayList = new ArrayList<Pair<Date, Integer>>();
                    DateFormat df = new SimpleDateFormat("yyyyMMdd");
                    for (DataSnapshot date : name.getChildren()) {
                        try {
                            pairArrayList.add(new Pair<Date,Integer>(df.parse(date.getKey()), date.getValue(Integer.class)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    Meme m = new Meme(name.getKey(), pairArrayList);
                    memes.add(m);
                }
                Collections.sort(memes);
                s.execute(memes);
                listAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
    }
}
