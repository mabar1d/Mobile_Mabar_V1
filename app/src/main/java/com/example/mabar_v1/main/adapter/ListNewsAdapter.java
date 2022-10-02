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
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mabar_v1.R;
import com.example.mabar_v1.main.DetailNewsActivity;
import com.example.mabar_v1.retrofit.model.GetListNewsResponseModel;
import com.example.mabar_v1.utility.GlobalMethod;

import java.util.ArrayList;
import java.util.List;

public class ListNewsAdapter extends RecyclerView.Adapter<ListNewsAdapter.NewsViewHolder> {
    private Context context;
    private List<GetListNewsResponseModel.Data> dataNews = new ArrayList<>();
    private GlobalMethod globalMethod;

    public ListNewsAdapter(Context context, List<GetListNewsResponseModel.Data> dataNews) {
        this.context = context;
        this.dataNews = dataNews;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_news,parent,false);
        globalMethod = new GlobalMethod();
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.judulNews.setText(dataNews.get(position).getTitle());
        holder.categoryNews.setText(dataNews.get(position).getNewsCategoryName());

        holder.infoNews.setText(dataNews.get(position).getFirstname() + " - " + dataNews.get(position).getDiffCreatedAt() );

        CircularProgressDrawable cp = new CircularProgressDrawable(context);
        cp.setStrokeWidth(5f);
        //cp.setBackgroundColor(R.color.material_grey_300);
        cp.setColorSchemeColors(R.color.primary_color_black, R.color.material_grey_800, R.color.material_grey_700);
        cp.setCenterRadius(30f);
        cp.start();
        Glide.with(context)
                .load(dataNews.get(position).getImage())
                .placeholder(cp)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .into(holder.imageNews);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailNewsActivity.class);
                Bundle bun = new Bundle();
                bun.putString("id_news", String.valueOf(dataNews.get(position).getId()));

                i.putExtras(bun);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataNews.size();
    }


    public class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageNews;
        TextView judulNews,infoNews,categoryNews;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageNews = itemView.findViewById(R.id.image_news);
            judulNews = itemView.findViewById(R.id.judul_news);
            infoNews = itemView.findViewById(R.id.info_news);
            categoryNews = itemView.findViewById(R.id.tv_category);
        }
    }
}
