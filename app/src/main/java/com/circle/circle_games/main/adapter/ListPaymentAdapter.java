package com.circle.circle_games.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.circle.circle_games.R;
import com.circle.circle_games.main.GeneralSearchTournamentActivity;
import com.circle.circle_games.retrofit.model.DataItem;
import com.circle.circle_games.retrofit.model.ListPaymentResponseModel;
import com.circle.circle_games.team.MemberTeamListTournamentActivity;
import com.circle.circle_games.utility.GlobalMethod;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class ListPaymentAdapter extends RecyclerView.Adapter<ListPaymentAdapter.GameViewHolder> {
    private Context context;
    private List<ListPaymentResponseModel.Data> dataPayment = new ArrayList<>();
    private GlobalMethod globalMethod;

    public ListPaymentAdapter(Context context, List<ListPaymentResponseModel.Data> dataPayment) {
        this.context = context;
        this.dataPayment = dataPayment;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_payment,parent,false);
        globalMethod = new GlobalMethod();
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.titlePayment.setText(dataPayment.get(position).getPayment());
        holder.idPayment.setText(dataPayment.get(position).getIdPayment());

        if (dataPayment.get(position).getPayment().equalsIgnoreCase("Link aja")){
            Glide.with(context)
                    .load(R.drawable.ic_link_aja)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.skipMemoryCache(true)
                    .into(holder.ivPayment);
        }else if (dataPayment.get(position).getPayment().equalsIgnoreCase("Gopay")){
            Glide.with(context)
                    .load(R.drawable.ic_gopay)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.skipMemoryCache(true)
                    .into(holder.ivPayment);

        }else if (dataPayment.get(position).getPayment().equalsIgnoreCase("Ovo")){
            Glide.with(context)
                    .load(R.drawable.ic_ovo)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.skipMemoryCache(true)
                    .into(holder.ivPayment);

        }else if (dataPayment.get(position).getPayment().equalsIgnoreCase("Dana")){
            Glide.with(context)
                    .load(R.drawable.ic_dana)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.skipMemoryCache(true)
                    .into(holder.ivPayment);

        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Coming Soon!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataPayment.size();
    }


    public class GameViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPayment;
        TextView titlePayment,idPayment;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPayment = itemView.findViewById(R.id.iv_payment);
            titlePayment = itemView.findViewById(R.id.title_payment);
            idPayment = itemView.findViewById(R.id.id_payment);
        }

    }
}
