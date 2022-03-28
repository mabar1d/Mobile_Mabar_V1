package com.example.mabar_v1.main.adapter;

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
import com.example.mabar_v1.R;
import com.example.mabar_v1.retrofit.model.ListPersonnelResponseModel;
import com.example.mabar_v1.utility.GlobalMethod;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class ListPersonAddedAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ListPersonnelResponseModel.Data> dataPerson = new ArrayList<>();
    private GlobalMethod globalMethod;
    private final ListPersonAddedAdapter.OnItemClickListener listener;

    public ListPersonAddedAdapter(Context context, List<ListPersonnelResponseModel.Data> dataPerson,
                                  ListPersonAddedAdapter.OnItemClickListener listener) {
        this.context = context;
        this.dataPerson = dataPerson;
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(ListPersonnelResponseModel.Data item, int position);
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
        ListPersonnelResponseModel.Data dataModel  = dataPerson.get(position);
        final ListPersonAddedAdapter.PersonViewHolder holderItem = (ListPersonAddedAdapter.PersonViewHolder)holder;

        //postFinancingHolder.rbAsuransi.setEnabled(false);


        /*if (dataModel.getOnClick() == true){
            holderItem.cvPerson.setBackgroundColor(Color.parseColor("#ededed"));
            //postFinancingHolder.rbAsuransi.setChecked(true);
        }else {
           // postFinancingHolder.rbAsuransi.setChecked(false);
            holderItem.cvPerson.setBackgroundColor(context.getResources().getColor(R.color.white));
        }*/

        holderItem.tvPersonName.setText(dataModel.getUsername());

        /*holderItem.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataPerson.remove(position);
                notifyDataSetChanged();
            }
        });*/
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
        public void bind(ListPersonnelResponseModel.Data modelList, ListPersonAddedAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(modelList, getAdapterPosition());
                }
            });
        }
    }
}
