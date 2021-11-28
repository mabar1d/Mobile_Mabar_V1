package com.example.mabar_v1.splash;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mabar_v1.R;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.signup.SignUpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import egolabsapps.basicodemine.videolayout.VideoLayout;

public class SplashScreen1 extends AppCompatActivity {

   /* @BindView(R.id.videoLayout)
    VideoLayout videoLayout;*/
    @BindView(R.id.videoView) VideoView videoView;

    @BindView(R.id.c_layout_video)
    ConstraintLayout clVideo;
    private String pathVideo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        getPackageName();
        pathVideo = "android.resource://"+getPackageName()+"/"+R.raw.splash_video;

        /*videoLayout = new VideoLayout(this);
        videoLayout.setGravity(VideoLayout.VGravity.centerCrop);
        videoLayout.setIsLoop(true);
        videoLayout.setSound(true);
        videoLayout.setPathOrUrl(pathVideo);
        clVideo.addView(videoLayout);*/


        Uri uri = Uri.parse(pathVideo);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

    }

}
