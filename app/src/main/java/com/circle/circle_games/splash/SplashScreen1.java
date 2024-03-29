package com.circle.circle_games.splash;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.circle.circle_games.BuildConfig;
import com.circle.circle_games.MainActivity;
import com.circle.circle_games.host.HostManageTournamentActivity;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.main.DetailTournamentActivity;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.CheckVersionResponseModel;
import com.circle.circle_games.retrofit.model.DataItem;
import com.circle.circle_games.retrofit.model.ResponseListGame;
import com.circle.circle_games.room.viewmodel.MasterViewModel;
import com.circle.circle_games.team.DetailTeamInfoActivity;
import com.circle.circle_games.utility.CurrencyEditTextWatcher;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.circle.circle_games.MainActivity;
import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.utility.SessionUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen1 extends AppCompatActivity {


    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    private SessionUser sess;
    private GlobalMethod gm;

    private JSONArray arrayCheckVersionDb = new JSONArray();

    private static final int PERMISSION_REQUEST_CODE = 123;

    private String[] permissions;

    private int permissionIndex = 0;
    private boolean downloadMstGame = false;
    private boolean downloadMstMenu = false;
    private MasterViewModel viewModel;
    ArrayList<String> dbType = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        sess = new SessionUser(this);
        gm = new GlobalMethod();
        viewModel = ViewModelProviders.of(this).get(MasterViewModel.class);

        dbType = gm.getTypeDatabase();
        tvVersion.setText("Version "+BuildConfig.VERSION_NAME);
        ivLogo.post(new Runnable() {
            @Override
            public void run() {
                startImageAnimation();
            }
        });

        if (Build.VERSION.SDK_INT >= 33) {
            permissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.POST_NOTIFICATIONS
            };

        }else {
            permissions = new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            };
        }



        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        requestPermission(permissions[0]);



    }

    private void requestPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                showPermissionExplanation(permission);
            } else {
                    ActivityCompat.requestPermissions(this, new String[]{permission}, PERMISSION_REQUEST_CODE);
            }
        } else {
            // Permission is already granted, proceed with the next permission
            requestNextPermission();
        }
    }
    private void requestNextPermission() {
        permissionIndex++;
        if (permissionIndex < permissions.length) {
            requestPermission(permissions[permissionIndex]);
        } else {
            cekSession();
        }
    }

    private void showPermissionExplanation(final String permission) {
        gm.showDialogConfirmation(SplashScreen1.this, "Permission Needed", "This app needs the permission to function properly",
                "Ok", "Exit", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gm.dismissDialogConfirmation();
                        ActivityCompat.requestPermissions(SplashScreen1.this,
                                new String[]{permission}, PERMISSION_REQUEST_CODE);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gm.dismissDialogConfirmation();
                        finish();
                    }
                });
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with the next permission
                requestNextPermission();
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkAllDownload(){
        boolean done = true;

        if (!downloadMstGame){
            done = false;
        }
        if (!downloadMstMenu){
            done = false;
        }

        if (done){
            final Intent mainIntent = new Intent(SplashScreen1.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }

    private void checkVersionLokal(){
        ArrayList<String> dbType = gm.getTypeDatabase();
        try {
            for (int i = 0; i < arrayCheckVersionDb.length();i++){
                String dbTypeApi = arrayCheckVersionDb.getJSONObject(i).getString("type");
                String dbVersionApi = arrayCheckVersionDb.getJSONObject(i).getString("version");
                for (int j = 0; j < dbType.size(); j++) {
                    String dbTypeLokal = dbType.get(j);

                    if (dbTypeApi.equals(dbTypeLokal)) {

                        if (dbTypeLokal.equals("mst_game")) {
                            String versionDbLokal = gm.getDbLokalVersion(dbTypeApi);

                            if (!versionDbLokal.equals(dbVersionApi)) {
                                gm.showDialogConfirmation(SplashScreen1.this, "Download Master Game?", "Download Now?", "Download", "Cancel", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        gm.dismissDialogConfirmation();
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        gm.dismissDialogConfirmation();
                                    }
                                });
                            }

                        } else if (dbTypeLokal.equals("mst_menu")) {
                            String dbVersionMenu = "1.0"; //get DB Room version
                            if (!dbVersionMenu.equals(dbVersionApi)) {
                                gm.showDialogConfirmation(SplashScreen1.this, "Download Master Menu?", "Download Now?", "Download", "Cancel", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        gm.dismissDialogConfirmation();
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        gm.dismissDialogConfirmation();
                                    }
                                });
                            }
                        }


                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void checkVersion(){
        try {
            Call<CheckVersionResponseModel> req = RetrofitConfig.getApiServices("").checkVersion(sess.getString("id_user"));
            req.enqueue(new Callback<CheckVersionResponseModel>() {
                @Override
                public void onResponse(Call<CheckVersionResponseModel> call, Response<CheckVersionResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            if (response.body().getData().getVersionApk().equals(BuildConfig.VERSION_NAME)){
                                //Cek Version Database
                                for (int i = 0; i < response.body().getData().getVersionDatabase().size();i++){
                                    arrayCheckVersionDb.put(response.body().getData().getVersionDatabase().get(i));
                                    String dbTypeApi = response.body().getData().getVersionDatabase().get(i).getType();
                                    String dbVersionApi = response.body().getData().getVersionDatabase().get(i).getVersion();

                                for (int j = 0; j < dbType.size();j++){
                                    String dbTypeLokal = dbType.get(j);

                                    if (dbTypeLokal.equals(dbTypeApi)){
                                        dbType.remove(j);
                                        dbType.size();

                                        if (dbTypeLokal.equals("mst_game")){
                                            String dbVersionGame = gm.getDbLokalVersion(dbTypeLokal);
                                            if (!dbVersionGame.equals(dbVersionApi)){

                                                getListGame(sess.getString("id_user"),"","0");

                                            }else {
                                                downloadMstGame = true;
                                            }

                                        }else if (dbTypeLokal.equals("mst_menu")){
                                            String dbVersionMenu = gm.getDbLokalVersion(dbTypeLokal);
                                            if (!dbVersionMenu.equals(dbVersionApi)){
                                                gm.showDialogConfirmation(SplashScreen1.this, "Download Master Menu?", "Download Now?", "Download", "Cancel", new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        gm.dismissDialogConfirmation();
                                                        downloadMstMenu = true;
                                                        //download master menu
                                                    }
                                                }, new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        gm.dismissDialogConfirmation();
                                                    }
                                                });
                                            }else {
                                                downloadMstMenu = true;
                                            }
                                        }


                                    }

                                }

                                }

                                checkAllDownload();


                            }else {
                                gm.showDialogConfirmation(SplashScreen1.this, "Update Available!", "Update Your Apps?",
                                        "Update", "Exit", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                final String appPackageName =/* getPackageName()*/"com.mobile.legends"; // getPackageName() from Context or Activity object
                                                try {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                                } catch (android.content.ActivityNotFoundException anfe) {
                                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                                }
                                                gm.dismissDialogConfirmation();
                                            }
                                        }, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                gm.dismissDialogConfirmation();
                                                finishAffinity();
                                            }
                                        });
                            }


                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(SplashScreen1.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SplashScreen1.this, "Gagal Check Version", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<CheckVersionResponseModel> call, Throwable t) {
                    Toast.makeText(SplashScreen1.this, "Gagal Check Version", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);


                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //master game
    private void getListGame(String userId,String search,String page){

        try {
            Call<ResponseListGame> req = RetrofitConfig.getApiServices("").getListGame(userId, search, page);
            req.enqueue(new Callback<ResponseListGame>() {
                @Override
                public void onResponse(Call<ResponseListGame> call, Response<ResponseListGame> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            viewModel.deleteAllGame();

                            for(int i = 0; i < response.body().getData().size(); i++){
                                DataItem di = new DataItem(
                                        response.body().getData().get(i).getImage(),
                                        response.body().getData().get(i).getUpdatedAt(),
                                        response.body().getData().get(i).getCreatedAt(),
                                        response.body().getData().get(i).getId(),
                                        response.body().getData().get(i).getTitle()
                                );
                                viewModel.insert(di);
                            }
                            downloadMstGame = true;
                            checkAllDownload();

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(SplashScreen1.this, desc, Toast.LENGTH_SHORT).show();
                            sess.clearSess();
                            Intent i = new Intent(SplashScreen1.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(SplashScreen1.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(SplashScreen1.this, "Failed Request List Games", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(Call<ResponseListGame> call, Throwable t) {
                    Toast.makeText(SplashScreen1.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }
    //master menu

    private void cekSession(){

        if (sess.getString("token").equalsIgnoreCase("")){
            Intent i = new Intent(SplashScreen1.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }else {
            checkVersion();

        }
    }

    /*@SuppressLint("MissingSuperCall")
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
            ActivityCompat.requestPermissions(this, permissions, ALL_PERMISSIONS);
            Toast.makeText(this, "All permission is needed to run the app", Toast.LENGTH_SHORT).show();
            finish();
        }else {
            cekSession();
        }

    }*/

    private void startImageAnimation() {
        ObjectAnimator animation = ObjectAnimator.ofFloat(ivLogo, "translationY",-(ivLogo.getHeight()), 0);
        animation.setDuration(1100);
        animation.start();
    }


}
