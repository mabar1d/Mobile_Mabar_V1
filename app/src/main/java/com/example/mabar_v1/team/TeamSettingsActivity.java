package com.example.mabar_v1.team;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asura.library.posters.Poster;
import com.asura.library.posters.RemoteImage;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mabar_v1.R;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.main.DetailTournamentActivity;
import com.example.mabar_v1.main.adapter.ListGameAdapter;
import com.example.mabar_v1.main.adapter.ListTeamAdapter;
import com.example.mabar_v1.profile.DetailProfileAccountActivity;
import com.example.mabar_v1.retrofit.RetrofitConfig;
import com.example.mabar_v1.retrofit.model.GetListTournamentResponseModel;
import com.example.mabar_v1.retrofit.model.GetTeamInfoResponseModel;
import com.example.mabar_v1.retrofit.model.PersonnelResponseModel;
import com.example.mabar_v1.retrofit.model.ResponseListGame;
import com.example.mabar_v1.retrofit.model.SuccessResponseDefaultModel;
import com.example.mabar_v1.splash.SplashScreen1;
import com.example.mabar_v1.utility.GlobalMethod;
import com.example.mabar_v1.utility.SessionUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamSettingsActivity extends AppCompatActivity {

    @BindView(R.id.btn_create_team)
    TextView btnCreateTeam;
    @BindView(R.id.btn_edit_team)
    TextView btnEditTeam;
    @BindView(R.id.cv_team)
    CardView cvTeam;

    @BindView(R.id.civ_team)
    CircularImageView civTeam;
    @BindView(R.id.tv_name_team)
    TextView tvTeamName;
    @BindView(R.id.tv_team_info)
    TextView tvTeamInfo;
    @BindView(R.id.tv_team_id)
    TextView tvTeamId;
    @BindView(R.id.btn_delete_team)
    ImageView btnDeleteTeam;

    @BindView(R.id.btn_manage_team)
    TextView btnManageTeam;

    private String teamId = "";

    private GlobalMethod gm;
    private SessionUser sess;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_settings);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        gm = new GlobalMethod();
        sess = new SessionUser(this);

        getDataPerson();

        btnCreateTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TeamSettingsActivity.this, CreateTeamActivity.class);
                startActivity(i);
            }
        });

        btnEditTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TeamSettingsActivity.this, EditTeamActivity.class);
                startActivity(i);
            }
        });

        btnManageTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TeamSettingsActivity.this, ManageTeamActivity.class);
                Bundle bun = new Bundle();
                bun.putString("id_team", teamId);
                i.putExtras(bun);
                startActivity(i);
            }
        });

        btnDeleteTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gm.showDialogConfirmation(TeamSettingsActivity.this, "Delete Team?", "Are you sure?",
                        "Delete", "Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteTeam(teamId);
                                gm.dismissDialogConfirmation();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                gm.dismissDialogConfirmation();
                            }
                        });

            }
        });

    }

    private void getDataPerson(){
        ProgressDialog progress = new ProgressDialog(TeamSettingsActivity.this);
        progress.setMessage("Getting Info...");
        progress.show();
        try {
            Call<PersonnelResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getPersonnel(sess.getString("id_user"));
            req.enqueue(new Callback<PersonnelResponseModel>() {
                @Override
                public void onResponse(Call<PersonnelResponseModel> call, Response<PersonnelResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            if (response.body().getData().getTeamId()!= null){
                                btnCreateTeam.setVisibility(View.GONE);
                                btnEditTeam.setVisibility(View.VISIBLE);
                                btnManageTeam.setVisibility(View.VISIBLE);
                                teamId = response.body().getData().getTeamId();
                                getDataTeam(teamId);
                            }else {
                                btnCreateTeam.setVisibility(View.VISIBLE);
                                btnEditTeam.setVisibility(View.GONE);
                                btnManageTeam.setVisibility(View.GONE);

                                cvTeam.setVisibility(View.INVISIBLE);
                                btnDeleteTeam.setVisibility(View.INVISIBLE);
                            }


                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(TeamSettingsActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(TeamSettingsActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(TeamSettingsActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(TeamSettingsActivity.this, "Failed Request Profil Info", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<PersonnelResponseModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(TeamSettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteTeam(String idTeam){
        ProgressDialog progress = new ProgressDialog(TeamSettingsActivity.this);
        progress.setMessage("Getting Info...");
        progress.show();
        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiServices(sess.getString("token")).deleteTeam(sess.getString("id_user"),idTeam);
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            String desc = response.body().getDesc();
                            Toast.makeText(TeamSettingsActivity.this, desc, Toast.LENGTH_SHORT).show();
                            finish();

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(TeamSettingsActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(TeamSettingsActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(TeamSettingsActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(TeamSettingsActivity.this, "Failed Request Profil Info", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(TeamSettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getDataTeam(String teamId){
        ProgressDialog progress = new ProgressDialog(TeamSettingsActivity.this);
        progress.setMessage("Getting Info...");
        progress.show();
        try {
            Call<GetTeamInfoResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getInfoTeam(sess.getString("id_user"),teamId);
            req.enqueue(new Callback<GetTeamInfoResponseModel>() {
                @Override
                public void onResponse(Call<GetTeamInfoResponseModel> call, Response<GetTeamInfoResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            Glide.with(TeamSettingsActivity.this)
                                    .load(response.body().getData().getImage())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(civTeam);

                            tvTeamName.setText(response.body().getData().getName());
                            tvTeamInfo.setText(response.body().getData().getInfo());
                            tvTeamId.setText("Team Id: "+response.body().getData().getId());

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(TeamSettingsActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(TeamSettingsActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(TeamSettingsActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(TeamSettingsActivity.this, "Failed Request Team Info", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<GetTeamInfoResponseModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(TeamSettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}