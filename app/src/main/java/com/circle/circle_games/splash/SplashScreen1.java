package com.circle.circle_games.splash;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
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

import java.util.HashMap;
import java.util.Map;

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
    private boolean permissionGranted;

    public static int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    public static int MY_PERMISSIONS_REQUEST_STORAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String[] permissions = new String[]{CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE};

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

        if(ActivityCompat.checkSelfPermission(this, CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        {
            cekSession();
        }else {
            Toast.makeText(this, "All permission is needed to run the app", Toast.LENGTH_LONG).show();
        }



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


    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        int index = 0;
        Map<String, Integer> PermissionsMap = new HashMap<String, Integer>();
        for (String permission : permissions){
            PermissionsMap.put(permission, grantResults[index]);
            index++;
        }

        if((PermissionsMap.get(CAMERA) != 0)
                || PermissionsMap.get(WRITE_EXTERNAL_STORAGE) != 0
                || PermissionsMap.get(READ_EXTERNAL_STORAGE) != 0){
            Toast.makeText(this, "All permission is needed to run the app", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            cekSession();
        }

    }


}
