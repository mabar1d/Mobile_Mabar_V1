package com.circle.circle_games.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class ListPersonAddedAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ListPersonnelNotMemberResponseModel.Data> dataPerson = new ArrayList<>();
    private GlobalMethod globalMethod;
    private final ListPersonAddedAdapter.OnItemClickListener listener;

    public ListPersonAddedAdapter(Context context, List<ListPersonnelNotMemberResponseModel.Data> dataPerson,
                                  ListPersonAddedAdapter.OnItemClickListener listener) {
        this.context = context;
        this.dataPerson = dataPerson;
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(ListPersonnelNotMemberResponseModel.Data item, int position);
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_card_person_added,parent,false);
        globalMethod = new GlobalMethod();
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ListPersonnelNotMemberResponseModel.Data dataModel  = dataPerson.get(position);
        final ListPersonAddedAdapter.PersonViewHolder holderItem = (ListPersonAddedAdapter.PersonViewHolder)holder;

        //postFinancingHolder.rbAsuransi.setEnabled(false);


        holderItem.tvPersonName.setText(dataModel.getUsername());


        Glide.with(context)
                /*.load(dataPerson.get(position).getImage())*/
                .load(R.drawable.person_photo)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .into(holderItem.civPerson);

        holderItem.bind(dataPerson.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return dataPerson.size();
    }


    public class PersonViewHolder extends RecyclerView.ViewHolder {
        CircularImageView civPerson;
        TextView tvPersonName;
        CardView cvPerson;
        ImageView ivCancel;
        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPersonName = itemView.findViewById(R.id.tv_name_person);
            civPerson = itemView.findViewById(R.id.civ_person);
            cvPerson = itemView.findViewById(R.id.card_person);
            ivCancel = itemView.findViewById(R.id.btn_cancel);
        }
        public void bind(ListPersonnelNotMemberResponseModel.Data modelList, ListPersonAddedAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(modelList, getAdapterPosition());
                }
            });
        }
    }
}
