package com.circle.circle_games.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.circle.circle_games.R;
import com.circle.circle_games.main.DetailNewsActivity;
import com.circle.circle_games.main.DetailVideosActivity;
import com.circle.circle_games.main.VideosActivity;
import com.circle.circle_games.retrofit.model.GetListNewsResponseModel;
import com.circle.circle_games.retrofit.model.GetListVideosResponseModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class ListVideosAdapter extends RecyclerView.Adapter<ListVideosAdapter.VideoViewHolder> {
    private Context context;
    private List<GetListVideosResponseModel.Data> dataVideo = new ArrayList<>();
    private GlobalMethod globalMethod;
    public static final int VIEW_TYPE_NORMAL = 1;
    DisplayMetrics displayMetrics = new DisplayMetrics();

    public ListVideosAdapter(Context context, List<GetListVideosResponseModel.Data> dataVideo) {
        this.context = context;
        this.dataVideo = dataVideo;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_videos,parent,false);
        globalMethod = new GlobalMethod();
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.judulVideo.setText(dataVideo.get(position).getTitle());

        holder.infoVideo.setText(dataVideo.get(position).getFirstname() + " - " + dataVideo.get(position).getDiffCreatedAt() );


        /*((VideosActivity) context).addLifeCycleCallBack(holder.playerView);

        holder.playerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = dataVideo.get(position).getLink();
                youTubePlayer.cueVideo(videoId, 0);
            }
        });*/

        CircularProgressDrawable cp = new CircularProgressDrawable(context);
        cp.setStrokeWidth(5f);
        //cp.setBackgroundColor(R.color.material_grey_300);
        cp.setColorSchemeColors(R.color.primary_color_black, R.color.material_grey_800, R.color.material_grey_900);
        cp.setCenterRadius(30f);
        cp.start();
        Glide.with(context)
                .load(dataVideo.get(position).getImage())
                .placeholder(cp)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.img_not_found)
                //.skipMemoryCache(true)
                .into(holder.ivVideo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, DetailVideosActivity.class);
                Bundle bun = new Bundle();
                bun.putString("id_video", String.valueOf(dataVideo.get(position).getVideoId()));

                i.putExtras(bun);
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_NORMAL;
    }

    public void setItems(List<GetListVideosResponseModel.Data> youtubeVideos) {
        dataVideo = youtubeVideos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (dataVideo != null && dataVideo.size() > 0) {
            return dataVideo.size();
        } else {
            return 1;
        }
    }


    public class VideoViewHolder extends RecyclerView.ViewHolder {
        YouTubePlayerView playerView;
        ImageView ivVideo;
        TextView judulVideo,infoVideo;
        LinearLayout llYtView;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
           // playerView = itemView.findViewById(R.id.youtube_player_view);
            ivVideo = itemView.findViewById(R.id.iv_video);
            judulVideo = itemView.findViewById(R.id.judul_videos);
            infoVideo = itemView.findViewById(R.id.tv_info_video);
            llYtView = itemView.findViewById(R.id.ll_ytView);
        }
    }
}
