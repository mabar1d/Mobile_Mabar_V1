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
import com.example.mabar_v1.retrofit.model.GetListTeamTournamentResponseModel;
import com.example.mabar_v1.retrofit.model.GetListTournamentResponseModel;
import com.example.mabar_v1.utility.GlobalMethod;

import java.util.ArrayList;
import java.util.List;

public class ListTeamTournamentAdapter extends RecyclerView.Adapter<ListTeamTournamentAdapter.TournamentViewHolder> {
    private Context context;
    private List<GetListTeamTournamentResponseModel.Data> dataTournament = new ArrayList<>();
    private GlobalMethod gm;

    public ListTeamTournamentAdapter(Context context, List<GetListTeamTournamentResponseModel.Data> dataTournament) {
        this.context = context;
        this.dataTournament = dataTournament;
    }

    @NonNull
    @Override
    public TournamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_team_tournament,parent,false);
        gm = new GlobalMethod();
        return new TournamentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.judulTourney.setText(dataTournament.get(position).getTournamentName());

        holder.startDate.setText(gm.setDateIndonesia(2,dataTournament.get(position).getTournamentStartDate()));
        Glide.with(context)
                .load(dataTournament.get(position).getTournamentImage())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .into(holder.imageTourney);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailTournamentActivity.class);
                Bundle bun = new Bundle();
                bun.putString("id_tournament", String.valueOf(dataTournament.get(position).getTournamentId()));
                bun.putString("judul_game", (dataTournament.get(position).getTournamentName()));
                i.putExtras(bun);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataTournament.size();
    }


    public class TournamentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageTourney;
        TextView judulTourney,startDate,btnDetail;
        public TournamentViewHolder(@NonNull View itemView) {
            super(itemView);
            judulTourney = itemView.findViewById(R.id.judul_tourney);
            imageTourney = itemView.findViewById(R.id.image_tourney);
            startDate = itemView.findViewById(R.id.tv_start_date);
            btnDetail = itemView.findViewById(R.id.btn_detail);
        }
    }
}
