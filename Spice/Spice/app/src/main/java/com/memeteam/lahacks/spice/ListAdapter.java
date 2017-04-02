package com.memeteam.lahacks.spice;

/**
 * Created by hp on 4/1/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Aayush on 9/28/2016.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {

    public List<Meme> memes;

    public ListAdapter(List<Meme> memes) {
        this.memes = memes;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        This "inflates" the views, using the layout R.layout.row_view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Meme currMeme = memes.get(position);
        holder.nameView.setText("" + position + "." + currMeme.name);
        holder.indexView.setText("Index: lol idk");
    }

    @Override
    public int getItemCount() {
        return memes.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView nameView;
        TextView indexView;

        public CustomViewHolder (View view) {
            super(view);
            nameView = (TextView) view.findViewById(R.id.nameView);
            indexView = (TextView) view.findViewById(R.id.indexView);
        }
    }
}
