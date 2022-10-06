package com.circle.circle_games.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.circle.circle_games.retrofit.model.GetListRequestJoinTeamResponseModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.R;
import com.circle.circle_games.retrofit.model.GetListRequestJoinTeamResponseModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class ListPersonReqJoinTeamAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<GetListRequestJoinTeamResponseModel.Data> dataPerson = new ArrayList<>();
    private GlobalMethod globalMethod;
    private final ListPersonReqJoinTeamAdapter.OnItemClickListener listener;

    public ListPersonReqJoinTeamAdapter(Context context, List<GetListRequestJoinTeamResponseModel.Data> dataPerson,
                                        ListPersonReqJoinTeamAdapter.OnItemClickListener listener) {
        this.context = context;
        this.dataPerson = dataPerson;
        this.listener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(GetListRequestJoinTeamResponseModel.Data item, int position);
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_card_person_req_join,parent,false);
        globalMethod = new GlobalMethod();
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        GetListRequestJoinTeamResponseModel.Data dataModel  = dataPerson.get(position);
        final ListPersonReqJoinTeamAdapter.PersonViewHolder holderItem = (ListPersonReqJoinTeamAdapter.PersonViewHolder)holder;

        holderItem.tvPersonName.setText(dataModel.getUserRequestName());
        holderItem.tvIdPerson.setText(dataModel.getUserRequestId()+"");

        Glide.with(context)
                .load(R.drawable.person_photo)
                .into(holderItem.civPerson);

        holderItem.bind(dataPerson.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return dataPerson.size();
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
        public void bind(GetListRequestJoinTeamResponseModel.Data modelList, ListPersonReqJoinTeamAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(modelList, getAdapterPosition());
                }
            });
        }
    }
}
