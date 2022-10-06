package com.circle.circle_games.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.circle.circle_games.main.GeneralSearchTournamentActivity;
import com.circle.circle_games.R;
import com.circle.circle_games.main.GeneralSearchTournamentActivity;
import com.circle.circle_games.retrofit.model.DataItem;
import com.circle.circle_games.utility.GlobalMethod;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class ListGameAdapter extends RecyclerView.Adapter<ListGameAdapter.GameViewHolder> {
    private Context context;
    private List<DataItem> dataGame = new ArrayList<>();
    private GlobalMethod globalMethod;

    public ListGameAdapter(Context context, List<DataItem> dataGame) {
        this.context = context;
        this.dataGame = dataGame;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_game_home,parent,false);
        globalMethod = new GlobalMethod();
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.judulGame.setText(dataGame.get(position).getTitle());

        Glide.with(context)
                .load(dataGame.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .into(holder.civGame);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, GeneralSearchTournamentActivity.class);
                Bundle bun = new Bundle();
                bun.putString("id_game",dataGame.get(position).getId().toString());
                bun.putString("judul_game",dataGame.get(position).getTitle());
                i.putExtras(bun);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataGame.size();
    }


    public class GameViewHolder extends RecyclerView.ViewHolder {
        CircularImageView civGame;
        TextView judulGame;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            civGame = itemView.findViewById(R.id.civ_game);
            judulGame = itemView.findViewById(R.id.judul_game);
        }

    }
}
