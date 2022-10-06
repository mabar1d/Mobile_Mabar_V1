package com.circle.circle_games.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.circle.circle_games.MainActivity;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.utility.SessionUser;
import com.circle.circle_games.MainActivity;
import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.utility.SessionUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen1 extends AppCompatActivity {

   /* @BindView(R.id.videoLayout)
    VideoLayout videoLayout;*/
    @BindView(R.id.videoView) VideoView videoView;

    @BindView(R.id.c_layout_video)
    ConstraintLayout clVideo;
    private String pathVideo = "";
    private SessionUser sess;
    int ALL_PERMISSIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        ActivityCompat.requestPermissions(this, permissions, ALL_PERMISSIONS);


        /*Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) { ... }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) { ... }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) { ... }
                }).check();

        Dexter.withContext(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) { ... }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) { ... }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) { ... }
                }).check();*/


        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        sess = new SessionUser(this);

        //pathVideo = "android.resource://"+getPackageName()+"/"+R.raw.splash_video;

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
        /*Uri uri = Uri.parse(pathVideo);
        videoView.setVideoURI(uri);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });*/



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
            }, 5000);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Intent mainIntent = new Intent(SplashScreen1.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }, 5000);
        }
    }


}
