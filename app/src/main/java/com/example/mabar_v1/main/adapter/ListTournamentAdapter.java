package com.example.mabar_v1.main.adapter;

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
import com.example.mabar_v1.R;
import com.example.mabar_v1.retrofit.model.GetListTournamentResponseModel;
import com.example.mabar_v1.utility.GlobalMethod;


import java.util.ArrayList;
import java.util.List;

public class ListTournamentAdapter extends RecyclerView.Adapter<ListTournamentAdapter.TournamentViewHolder> {
    private Context context;
    private List<GetListTournamentResponseModel.Data> dataTournament = new ArrayList<>();
    private GlobalMethod globalMethod;

    public ListTournamentAdapter(Context context, List<GetListTournamentResponseModel.Data> dataTournament) {
        this.context = context;
        this.dataTournament = dataTournament;
    }

    @NonNull
    @Override
    public TournamentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_tournament_home,parent,false);
        globalMethod = new GlobalMethod();
        return new TournamentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TournamentViewHolder holder, int position) {
        holder.judulTourney.setText(dataTournament.get(position).getName());
        holder.prizeTourney.setText("Rp. "+dataTournament.get(position).getPrize());

        //holder.judulGame.setText(dataTournament.get(position).getData().get(position).getGame());
        /*Glide.with(context)
                .load(globalMethod.getUriImage(dataTv.get(position).getPosterPath()))
                .into(holder.imageTv);*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i = new Intent(context, .class);
                Bundle bun = new Bundle();
                bun.putString("title",dataTv.get(position).getName());
                bun.putString("backdrop_path",dataTv.get(position).getBackdropPath());
                bun.putString("deskripsi",dataTv.get(position).getOverview());
                bun.putString("release_date",dataTv.get(position).getFirstAirDate());
                bun.putString("popularity", String.valueOf(dataTv.get(position).getPopularity()));
                i.putExtras(bun);
                context.startActivity(i);*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataTournament.size();
    }


    public class TournamentViewHolder extends RecyclerView.ViewHolder {
        ImageView imageTourney, icTopTournament;
        TextView judulTourney,judulGame,prizeTourney,btnDetail;
        public TournamentViewHolder(@NonNull View itemView) {
            super(itemView);
            icTopTournament = itemView.findViewById(R.id.ic_top_tournament);
            judulTourney = itemView.findViewById(R.id.judul_tourney);
            prizeTourney = itemView.findViewById(R.id.prize_tourney);
            imageTourney = itemView.findViewById(R.id.image_tourney);
            judulGame = itemView.findViewById(R.id.judul_game);
            btnDetail = itemView.findViewById(R.id.btn_detail);
        }
    }
}
