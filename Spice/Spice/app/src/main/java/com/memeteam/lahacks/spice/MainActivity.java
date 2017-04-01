package com.memeteam.lahacks.spice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;
import com.memeteam.lahacks.spice.Fetcher.Meme;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fetcher.onCreate();
        TextView textView = (TextView) findViewById(R.id.marquee);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView .setSingleLine(true);
        textView.setMarqueeRepeatLimit(-1);
        textView.setFocusableInTouchMode(true);
        textView.setFocusable(true);

        List<Meme> memes = new ArrayList<>();
        memes.add(new Meme("poop meme"));
        memes.add(new Meme("your mum"));
        memes.add(new Meme("git ou' me cah brum brum"));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ListAdapter(memes));
    }
}
