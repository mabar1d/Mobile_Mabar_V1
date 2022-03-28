package com.example.mabar_v1.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mabar_v1.R;
import com.example.mabar_v1.main.DetailTournamentActivity;
import com.example.mabar_v1.retrofit.model.ListPersonnelResponseModel;
import com.example.mabar_v1.retrofit.model.ListTeamResponseModel;
import com.example.mabar_v1.utility.GlobalMethod;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class ListPersonAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ListPersonnelResponseModel.Data> dataTeam = new ArrayList<>();
    private GlobalMethod globalMethod;
    private final ListPersonAdapter.OnItemClickListener listener;

    public ListPersonAdapter(Context context, List<ListPersonnelResponseModel.Data> dataTeam,
                             ListPersonAdapter.OnItemClickListener listener) {
        this.context = context;
        this.dataTeam = dataTeam;
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(ListPersonnelResponseModel.Data item, int position);
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_card_person,parent,false);
        globalMethod = new GlobalMethod();
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListPersonnelResponseModel.Data dataModel  = dataTeam.get(position);
        final ListPersonAdapter.PersonViewHolder holderItem = (ListPersonAdapter.PersonViewHolder)holder;



        //postFinancingHolder.rbAsuransi.setEnabled(false);


        /*if (dataModel.getOnClick() == true){
            holderItem.cvPerson.setBackgroundColor(Color.parseColor("#ededed"));
            //postFinancingHolder.rbAsuransi.setChecked(true);
        }else {
           // postFinancingHolder.rbAsuransi.setChecked(false);
            holderItem.cvPerson.setBackgroundColor(context.getResources().getColor(R.color.white));
        }*/

        holderItem.tvPersonName.setText(dataTeam.get(position).getUsername());
        holderItem.tvIdPerson.setText("Id: "+ dataTeam.get(position).getUserId());

        Glide.with(context)
                /*.load(dataTeam.get(position).getImage())*/
                .load(R.drawable.person_photo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .into(holderItem.civPerson);

        holderItem.bind(dataTeam.get(position), listener);
    }



    @Override
    public int getItemCount() {
        return dataTeam.size();
    }


    public class PersonViewHolder extends RecyclerView.ViewHolder {
        CircularImageView civPerson;
        TextView tvPersonName,tvIdPerson;
        CardView cvPerson;
        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPersonName = itemView.findViewById(R.id.tv_name_person);
            tvIdPerson = itemView.findViewById(R.id.tv_id_person);
            civPerson = itemView.findViewById(R.id.civ_person);
            cvPerson = itemView.findViewById(R.id.card_person);
        }
        public void bind(ListPersonnelResponseModel.Data modelList, ListPersonAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(modelList, getAdapterPosition());
                }
            });
        }
    }
}
