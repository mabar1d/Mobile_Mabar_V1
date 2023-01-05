package com.circle.circle_games.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
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
import com.circle.circle_games.retrofit.model.ListPersonnelNotMemberResponseModel;
import com.circle.circle_games.team.MemberTeamListTournamentActivity;
import com.circle.circle_games.utility.GlobalMethod;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class ListPaymentAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<ListPaymentResponseModel.Data> dataPayment = new ArrayList<>();
    private final ListPaymentAdapter.OnItemClickListener listener;
    private GlobalMethod globalMethod;
    private int lastCheckedPosition = -1;

    public ListPaymentAdapter(Context context, List<ListPaymentResponseModel.Data> dataPayment,ListPaymentAdapter.OnItemClickListener listener) {
        this.context = context;
        this.dataPayment = dataPayment;
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ListPaymentResponseModel.Data item, int position);
    }

    @NonNull
    @Override
    public PaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_payment,parent,false);
        globalMethod = new GlobalMethod();
        return new PaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        final ListPaymentAdapter.PaymentViewHolder holderItem = (ListPaymentAdapter.PaymentViewHolder)holder;


        //radio
        holderItem.radioClick.setChecked(position == lastCheckedPosition);


        holderItem.titlePayment.setText(dataPayment.get(position).getPayment());
        holderItem.idPayment.setText(dataPayment.get(position).getIdPayment());

        if (dataPayment.get(position).getPayment().equalsIgnoreCase("Link aja")){
            Glide.with(context)
                    .load(R.drawable.ic_link_aja)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holderItem.ivPayment);
        }else if (dataPayment.get(position).getPayment().equalsIgnoreCase("Gopay")){
            Glide.with(context)
                    .load(R.drawable.ic_gopay)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holderItem.ivPayment);

        }else if (dataPayment.get(position).getPayment().equalsIgnoreCase("Ovo")){
            Glide.with(context)
                    .load(R.drawable.ic_ovo)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holderItem.ivPayment);

        }else if (dataPayment.get(position).getPayment().equalsIgnoreCase("Dana")){
            Glide.with(context)
                    .load(R.drawable.ic_dana)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holderItem.ivPayment);

        }else if (dataPayment.get(position).getPayment().equalsIgnoreCase("Coin Circle")){
            Glide.with(context)
                    .load(R.drawable.ic_coin2)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(holderItem.ivPayment);

        }


        holderItem.bind(dataPayment.get(position), listener);


    }

    @Override
    public int getItemCount() {
        return dataPayment.size();
    }


    public class PaymentViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPayment;
        TextView titlePayment,idPayment;
        RadioButton radioClick;
        RelativeLayout rlItem;

        public PaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPayment = itemView.findViewById(R.id.iv_payment);
            titlePayment = itemView.findViewById(R.id.title_payment);
            idPayment = itemView.findViewById(R.id.id_payment);
            radioClick = itemView.findViewById(R.id.radio_click);
            rlItem = itemView.findViewById(R.id.rl_item);

        }

        public void bind(ListPaymentResponseModel.Data modelList, ListPaymentAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(modelList, getAdapterPosition());
                    if (getAdapterPosition() == lastCheckedPosition) {
                        radioClick.setChecked(false);
                        lastCheckedPosition = -1;
                    }
                    else {
                        lastCheckedPosition = getAdapterPosition();
                        notifyDataSetChanged();
                    }
                }
            });
        }

    }
}
