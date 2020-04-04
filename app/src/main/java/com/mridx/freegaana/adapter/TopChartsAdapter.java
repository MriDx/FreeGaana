/*
 * Copyright (c) 2020.  by MriDx
 */

package com.mridx.freegaana.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mridx.freegaana.R;
import com.mridx.freegaana.activity.MainUI;
import com.mridx.freegaana.activity.PlaylistUI;
import com.mridx.freegaana.dataholder.TopChart;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TopChartsAdapter extends RecyclerView.Adapter<TopChartsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<TopChart> topChartsList = new ArrayList<>();

    public TopChartsAdapter(Context context, ArrayList<TopChart> topChartsList) {
        this.context = context;
        this.topChartsList = topChartsList;
    }

    @NonNull
    @Override
    public TopChartsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_view, null);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopChartsAdapter.MyViewHolder holder, int position) {
        TopChart topChart = topChartsList.get(position);
        holder.songNameView.setText(topChart.getTitle());
        Picasso.get()
                .load(topChart.getThumbnail())
                .into(holder.songThumbView);
    }

    @Override
    public int getItemCount() {
        return topChartsList.size();
    }

    public void setSongDataList(ArrayList<TopChart> topChartsSongs) {
        this.topChartsList = topChartsSongs;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView songThumbView;
        private AppCompatTextView songNameView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            songThumbView = itemView.findViewById(R.id.songThumbView);
            songNameView = itemView.findViewById(R.id.songNameView);
            itemView.findViewById(R.id.songInfoView).setVisibility(View.GONE);
            itemView.setOnClickListener(v -> handleClick(getAdapterPosition()));
        }
    }

    public void handleClick(int position) {

        ((MainUI)context).handleTopChartsClick(topChartsList.get(position));
    }
}
