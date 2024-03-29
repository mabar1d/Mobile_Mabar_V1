package com.circle.circle_games.team;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.GetTeamInfoResponseModel;
import com.circle.circle_games.retrofit.model.PersonnelResponseModel;
import com.circle.circle_games.retrofit.model.SuccessResponseDefaultModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.GetTeamInfoResponseModel;
import com.circle.circle_games.retrofit.model.PersonnelResponseModel;
import com.circle.circle_games.retrofit.model.SuccessResponseDefaultModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamSettingsActivity extends AppCompatActivity {

    @BindView(R.id.btn_create_team)
    RelativeLayout btnCreateTeam;
    @BindView(R.id.btn_edit_team)
    RelativeLayout btnEditTeam;
    @BindView(R.id.btn_manage_team)
    RelativeLayout btnManageTeam;

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
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.shimmer_load)
    ShimmerFrameLayout shimmerLoad;

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


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
                Bundle bun = new Bundle();
                bun.putString("id_team", teamId);
                i.putExtras(bun);
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
        gm.setShimmerLinearLayout(true,shimmerLoad,llContent);
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
                            gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
                            sess.clearSess();
                            Intent i = new Intent(TeamSettingsActivity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(TeamSettingsActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(TeamSettingsActivity.this, "Failed Request Profil Info", Toast.LENGTH_SHORT).show();
                        gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
                    }
                    gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
                }

                @Override
                public void onFailure(Call<PersonnelResponseModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(TeamSettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void deleteTeam(String idTeam){
        gm.showLoadingDialog(TeamSettingsActivity.this);
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
                            gm.dismissLoadingDialog();
                            sess.clearSess();
                            Intent i = new Intent(TeamSettingsActivity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(TeamSettingsActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(TeamSettingsActivity.this, "Failed Request Profil Info", Toast.LENGTH_SHORT).show();
                        gm.dismissLoadingDialog();
                    }
                    gm.dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(TeamSettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    gm.dismissLoadingDialog();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getDataTeam(String teamId){
        try {
            Call<GetTeamInfoResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getInfoTeam(sess.getString("id_user"),teamId);
            req.enqueue(new Callback<GetTeamInfoResponseModel>() {
                @Override
                public void onResponse(Call<GetTeamInfoResponseModel> call, Response<GetTeamInfoResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            CircularProgressDrawable cp = new CircularProgressDrawable(TeamSettingsActivity.this);
                            cp.setStrokeWidth(5f);
                            //cp.setBackgroundColor(R.color.material_grey_300);
                            cp.setColorSchemeColors(R.color.primary_color_black, R.color.material_grey_800, R.color.material_grey_900);
                            cp.setCenterRadius(30f);
                            cp.start();

                            Glide.with(TeamSettingsActivity.this)
                                    .load(response.body().getData().getImage())
                                    .placeholder(cp)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(civTeam);

                            tvTeamName.setText(response.body().getData().getName());
                            tvTeamInfo.setText(response.body().getData().getInfo());
                            tvTeamId.setText("Team Id: "+response.body().getData().getId());
                            gm.setShimmerLinearLayout(false,shimmerLoad,llContent);

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(TeamSettingsActivity.this, desc, Toast.LENGTH_SHORT).show();
                            gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
                            sess.clearSess();
                            Intent i = new Intent(TeamSettingsActivity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(TeamSettingsActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(TeamSettingsActivity.this, "Failed Request Team Info", Toast.LENGTH_SHORT).show();
                        gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
                    }
                    gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
                }

                @Override
                public void onFailure(Call<GetTeamInfoResponseModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(TeamSettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataPerson();
    }

}