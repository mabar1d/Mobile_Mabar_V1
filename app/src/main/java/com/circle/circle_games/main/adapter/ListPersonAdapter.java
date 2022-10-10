package com.circle.circle_games.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.circle.circle_games.R;
import com.circle.circle_games.retrofit.model.ListPersonnelNotMemberResponseModel;
import com.circle.circle_games.retrofit.model.ListPersonnelResponseModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class ListPersonAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ListPersonnelNotMemberResponseModel.Data> dataTeam = new ArrayList<>();
    private GlobalMethod globalMethod;
    private final ListPersonAdapter.OnItemClickListener listener;

    public ListPersonAdapter(Context context, List<ListPersonnelNotMemberResponseModel.Data> dataTeam,
                             ListPersonAdapter.OnItemClickListener listener) {
        this.context = context;
        this.dataTeam = dataTeam;
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(ListPersonnelNotMemberResponseModel.Data item, int position);
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
        ListPersonnelNotMemberResponseModel.Data dataModel  = dataTeam.get(position);
        final ListPersonAdapter.PersonViewHolder holderItem = (ListPersonAdapter.PersonViewHolder)holder;

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
        public void bind(ListPersonnelNotMemberResponseModel.Data modelList, ListPersonAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(modelList, getAdapterPosition());
                }
            });
        }
    }
}
