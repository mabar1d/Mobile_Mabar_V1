package com.example.mabar_v1.splash;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mabar_v1.MainActivity;
import com.example.mabar_v1.R;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.signup.SignUpActivity;
import com.example.mabar_v1.utility.SessionUser;

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
    private SessionUser sess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        sess = new SessionUser(this);

        pathVideo = "android.resource://"+getPackageName()+"/"+R.raw.splash_video;

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        /*videoLayout = new VideoLayout(this);
        videoLayout.setGravity(VideoLayout.VGravity.centerCrop);
        videoLayout.setIsLoop(true);
        videoLayout.setSound(true);
        videoLayout.setPathOrUrl(pathVideo);
        clVideo.addView(videoLayout);*/
        cekSession();
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

    private void cekSession(){
        if (sess.getString("token").equalsIgnoreCase("")){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Intent mainIntent = new Intent(SplashScreen1.this, LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, 10000);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Intent mainIntent = new Intent(SplashScreen1.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, 10000);
        }
    }


}
