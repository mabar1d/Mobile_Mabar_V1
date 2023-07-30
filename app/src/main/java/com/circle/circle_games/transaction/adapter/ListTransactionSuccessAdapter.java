package com.circle.circle_games.transaction.adapter;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.circle.circle_games.R;
import com.circle.circle_games.retrofit.model.ListPersonnelNotMemberResponseModel;
import com.circle.circle_games.transaction.model.GetListTransactionResponseModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class ListTransactionSuccessAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<GetListTransactionResponseModel.Data> dataTransaction = new ArrayList<>();
    private GlobalMethod globalMethod;
    private final ListTransactionSuccessAdapter.OnItemClickListener listener;
    private String status;

    public ListTransactionSuccessAdapter(Context context, String status, List<GetListTransactionResponseModel.Data> dataTransaction,
                                         ListTransactionSuccessAdapter.OnItemClickListener listener) {
        this.context = context;
        this.status = status;
        this.dataTransaction = dataTransaction;
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(GetListTransactionResponseModel.Data item, int position);
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_transaction,parent,false);
        globalMethod = new GlobalMethod();
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GetListTransactionResponseModel.Data dataModel  = dataTransaction.get(position);
        final ListTransactionSuccessAdapter.TransactionViewHolder holderItem = (ListTransactionSuccessAdapter.TransactionViewHolder)holder;


        holderItem.tvAmount.setText(globalMethod.formattedRupiah(dataModel.getGrossAmount()));
        holderItem.tvOrderId.setText(dataModel.getOrderId());
        holderItem.tvItemName.setText(dataModel.getItemName());
        holderItem.tvSettlementTime.setText(dataModel.getSettlementTime());
        holderItem.tvExpiryTime.setText(dataModel.getExpiryTime());
        if (dataModel.getPaymentTypeName().contains("Bank Transfer")){
            holderItem.llVaNumber.setVisibility(View.VISIBLE);
            holderItem.llBankName.setVisibility(View.VISIBLE);
            holderItem.tvVaNumber.setText(dataModel.getVaNumber());
            if (dataModel.getBankName() != null){
                holderItem.tvBankName.setText(dataModel.getBankName().toUpperCase());
            }

        }

        holderItem.tvVaNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", holderItem.tvVaNumber.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context,"Copied to Clipboard",Toast.LENGTH_SHORT).show();
            }
        });

        if (status.equalsIgnoreCase("success")){
            holderItem.tvStatus.setText("Success");
            holderItem.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.material_green_500));
        }else if (status.equalsIgnoreCase("pending")){
            holderItem.tvStatus.setText("Pending");
            holderItem.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.material_yellow_500));
        }else {
            holderItem.tvStatus.setText("Failed");
            holderItem.tvStatus.setTextColor(ContextCompat.getColor(context,R.color.material_red_500));
        }

        holderItem.ivCopyVa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", holderItem.tvVaNumber.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context,"Copied to Clipboard",Toast.LENGTH_SHORT).show();
            }
        });



        holderItem.bind(dataTransaction.get(position), listener);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clearAllData() {
        dataTransaction.clear(); // clear list
        notifyDataSetChanged(); // let your adapter know about the changes and reload view.
    }

    @Override
    public int getItemCount() {
        return dataTransaction.size();
    }


    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        ImageView ivJenisPayment,ivCopyVa;
        LinearLayout llVaNumber,llBankName;
        TextView tvJenisPayment,tvAmount,tvOrderId,tvItemName,tvSettlementTime,tvExpiryTime,tvStatus,tvVaNumber,tvBankName;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            ivJenisPayment = itemView.findViewById(R.id.iv_jenis);
            tvJenisPayment = itemView.findViewById(R.id.tv_jenis_payment);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvOrderId = itemView.findViewById(R.id.tv_order_id);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvSettlementTime = itemView.findViewById(R.id.tv_settlement_time);
            tvExpiryTime = itemView.findViewById(R.id.tv_expiry_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvVaNumber = itemView.findViewById(R.id.tv_va_number);
            llVaNumber = itemView.findViewById(R.id.ll_va_number);
            llBankName = itemView.findViewById(R.id.ll_bank_name);
            tvBankName = itemView.findViewById(R.id.tv_bank_name);
            ivCopyVa = itemView.findViewById(R.id.iv_copy);
        }
        public void bind(GetListTransactionResponseModel.Data modelList, ListTransactionSuccessAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(modelList, getAdapterPosition());
                }
            });
        }
    }
}
