package com.example.mabar_v1.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mabar_v1.R;
import com.example.mabar_v1.main.DetailTournamentActivity;
import com.example.mabar_v1.retrofit.model.GetListTournamentResponseModel;
import com.example.mabar_v1.retrofit.model.ListTeamResponseModel;
import com.example.mabar_v1.team.DetailTeamInfoActivity;
import com.example.mabar_v1.utility.GlobalMethod;
import com.mikhaellopez.circularimageview.CircularImageView;


import java.util.ArrayList;
import java.util.List;

public class ListTeamAdapter extends RecyclerView.Adapter<ListTeamAdapter.TeamViewHolder> {
    private Context context;
    private List<ListTeamResponseModel.Data> dataTeam = new ArrayList<>();
    private GlobalMethod globalMethod;

    public ListTeamAdapter(Context context, List<ListTeamResponseModel.Data> dataTeam) {
        this.context = context;
        this.dataTeam = dataTeam;
    }

    @NonNull
    @Override
    public TeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_card_team,parent,false);
        globalMethod = new GlobalMethod();
        return new TeamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tvTeamName.setText(dataTeam.get(position).getName());
        holder.tvTeamInfo.setText(dataTeam.get(position).getInfo());

        Glide.with(context)
                .load(dataTeam.get(position).getImage())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.civTeam);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailTeamInfoActivity.class);
                Bundle bun = new Bundle();
                bun.putString("id_team", String.valueOf(dataTeam.get(position).getId()));
                i.putExtras(bun);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataTeam.size();
    }


    public class TeamViewHolder extends RecyclerView.ViewHolder {
        CircularImageView civTeam;
        TextView tvTeamName,tvTeamInfo;
        public TeamViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTeamName = itemView.findViewById(R.id.tv_name_team);
            tvTeamInfo = itemView.findViewById(R.id.tv_team_info);
            civTeam = itemView.findViewById(R.id.civ_team);
        }
    }
}
