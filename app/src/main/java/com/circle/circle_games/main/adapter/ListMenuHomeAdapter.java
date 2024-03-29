package com.circle.circle_games.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.circle.circle_games.main.GeneralSearchTournamentActivity;
import com.circle.circle_games.main.NewsActivity;
import com.circle.circle_games.main.VideosActivity;
import com.circle.circle_games.retrofit.model.GetListMenuResponseModel;
import com.circle.circle_games.R;
import com.circle.circle_games.utility.GlobalMethod;

import java.util.ArrayList;
import java.util.List;

public class ListMenuHomeAdapter extends RecyclerView.Adapter<ListMenuHomeAdapter.MenuViewHolder> {
    private Context context;
    private List<GetListMenuResponseModel.Data> dataMenu = new ArrayList<>();
    private GlobalMethod globalMethod;

    public ListMenuHomeAdapter(Context context, List<GetListMenuResponseModel.Data> dataMenu) {
        this.context = context;
        this.dataMenu = dataMenu;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_menu_home,parent,false);
        globalMethod = new GlobalMethod();
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, @SuppressLint("RecyclerView") int position) {

        GetListMenuResponseModel.Data isi = dataMenu.get(position);

        holder.judulMenu.setText(isi.getTitle());

        //Mapping Menu
        if (isi.getId() == 1){
            //Tournament
            holder.ivMenu.setImageResource(R.drawable.ic_tournament_home);
            holder.llMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, GeneralSearchTournamentActivity.class);
                    Bundle bun = new Bundle();
                    bun.putString("id_game",null);
                    bun.putString("judul_game",null);
                    i.putExtras(bun);
                    context.startActivity(i);
                }
            });
        } else if (isi.getId() == 2) {
            //Top Up
            holder.ivMenu.setImageResource(R.drawable.ic_shop);
            holder.llMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context,"Coming Soon!!!",Toast.LENGTH_SHORT).show();
                    /*Intent i = new Intent(context, GeneralSearchTournamentActivity.class);
                    *//*Bundle bun = new Bundle();
                    i.putExtras(bun);*//*
                    context.startActivity(i);*/
                }
            });

        } else if (isi.getId() == 3) {
            //News Update
            holder.ivMenu.setImageResource(R.drawable.ic_news);
            holder.llMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, NewsActivity.class);
                    context.startActivity(i);
                }
            });

        } else if (isi.getId() == 4) {
            //Video
            holder.ivMenu.setImageResource(R.drawable.ic_video);
            holder.llMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, VideosActivity.class);
                    /*Bundle bun = new Bundle();
                    i.putExtras(bun);*/
                    context.startActivity(i);
                }
            });

        } else if (isi.getId() == 5) {
            //Chat
            holder.ivMenu.setImageResource(R.drawable.ic_chat);
            holder.llMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,"Coming Soon!!!",Toast.LENGTH_SHORT).show();
                    /*Intent i = new Intent(context, GeneralSearchTournamentActivity.class);
                    *//*Bundle bun = new Bundle();
                    i.putExtras(bun);*//*
                    context.startActivity(i);*/
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return dataMenu.size();
    }


    public class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView ivMenu;
        LinearLayout llMenu;
        TextView judulMenu;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMenu = itemView.findViewById(R.id.iv_menu);
            judulMenu = itemView.findViewById(R.id.judul_menu);
            llMenu = itemView.findViewById(R.id.ll_menu);
        }

    }
}
