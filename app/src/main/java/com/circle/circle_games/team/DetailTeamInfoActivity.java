package com.circle.circle_games.team;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.GetTeamInfoResponseModel;
import com.circle.circle_games.retrofit.model.SuccessResponseDefaultModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTeamInfoActivity extends AppCompatActivity {

    @BindView(R.id.civ_team)
    CircularImageView civTeam;
    @BindView(R.id.tv_name_team)
    TextView tvTeamName;
    @BindView(R.id.tv_team_info)
    TextView tvTeamInfo;
    @BindView(R.id.tv_team_id)
    TextView tvTeamId;
    @BindView(R.id.tv_team_leader)
    TextView tvTeamLeader;
    @BindView(R.id.tv_team_game)
    TextView tvTeamGame;
    @BindView(R.id.et_personnel)
    TextView tvMember;
    @BindView(R.id.btn_join_team)
    Button btnJoinTeam;
    @BindView(R.id.btn_fixture_team)
    Button btnTeamTournament;
    @BindView(R.id.btn_leave_team)
    Button btnLeaveTeam;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private String idTeam = "";
    private String nameTeam = "";
    private String flagTeam = "";
    private String valueMember = "";
    private SessionUser sess;
    private GlobalMethod gm;
    List<GetTeamInfoResponseModel.Data.Personnel> listPersonnel = new ArrayList<>();
    List<String> listPersonnelNew = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_team_info);
        ButterKnife.bind(this);
        Bundle b = getIntent().getExtras();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if(b != null) {
            idTeam = b.getString("id_team");
            nameTeam = b.getString("name_team");
            flagTeam = b.getString("flag_team");
        }
        sess = new SessionUser(this);
        gm = new GlobalMethod();

        getDataTeam(idTeam);

        if (flagTeam != null){
            btnLeaveTeam.setVisibility(View.VISIBLE);
            btnTeamTournament.setVisibility(View.VISIBLE);
            btnJoinTeam.setVisibility(View.GONE);
        }else {
            btnLeaveTeam.setVisibility(View.GONE);
            btnTeamTournament.setVisibility(View.GONE);
            btnJoinTeam.setVisibility(View.VISIBLE);
        }

        tvTitle.setText(nameTeam);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnJoinTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gm.showDialogConfirmation(DetailTeamInfoActivity.this, "Request Join Team?", "Are you sure?",
                        "Join", "Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestJoinTeam(idTeam);
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

        btnLeaveTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gm.showDialogConfirmation(DetailTeamInfoActivity.this, "Leave Team?", "Are you sure?",
                        "Leave", "Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                requestLeaveTeam();
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

        btnTeamTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailTeamInfoActivity.this, DetailTeamInfoActivity.class);
                Bundle bun = new Bundle();
                bun.putString("id_team", idTeam );
                bun.putString("flag_team", "Y");
                i.putExtras(bun);
                startActivity(i);
            }
        });

    }


    private void getDataTeam(String teamId){
        ProgressDialog progress = new ProgressDialog(DetailTeamInfoActivity.this);
        progress.setMessage("Getting Info...");
        progress.show();
        try {
            Call<GetTeamInfoResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getInfoTeam(sess.getString("id_user"),teamId);
            req.enqueue(new Callback<GetTeamInfoResponseModel>() {
                @Override
                public void onResponse(Call<GetTeamInfoResponseModel> call, Response<GetTeamInfoResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            Glide.with(DetailTeamInfoActivity.this)
                                    .load(response.body().getData().getImage())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(civTeam);

                            tvTeamName.setText(response.body().getData().getName());
                            tvTeamGame.setText(response.body().getData().getTitle_game());
                            tvTeamId.setText("Team Id: "+response.body().getData().getId());
                            tvTeamLeader.setText(response.body().getData().getUsernameAdmin());
                            tvTeamInfo.setText(response.body().getData().getInfo());

                            listPersonnel = response.body().getData().getPersonnel();
                            for (int i = 0; i < listPersonnel.size(); i++) {
                                listPersonnelNew.add(listPersonnel.get(i).getUsername());
                            }
                            valueMember = listPersonnelNew.toString();
                            if (valueMember.contains("[")) {
                                valueMember=valueMember.replace("[", "");
                            }
                            if (valueMember.contains("]")){
                                valueMember=valueMember.replace("]", "");
                            }
                            tvMember.setText(valueMember);


                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(DetailTeamInfoActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(DetailTeamInfoActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(DetailTeamInfoActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(DetailTeamInfoActivity.this, "Failed Request Team Info", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<GetTeamInfoResponseModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(DetailTeamInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void requestJoinTeam(String idTeam){
        ProgressDialog progress = new ProgressDialog(DetailTeamInfoActivity.this);
        progress.setMessage("Request Join...");
        progress.show();
        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiServices(sess.getString("token")).personnelReqJoinTeam(sess.getString("id_user"),idTeam);
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            String desc = response.body().getDesc();
                            Toast.makeText(DetailTeamInfoActivity.this, desc, Toast.LENGTH_SHORT).show();
                            //finish();

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(DetailTeamInfoActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(DetailTeamInfoActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(DetailTeamInfoActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(DetailTeamInfoActivity.this, "Failed Request Join team", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(DetailTeamInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void requestLeaveTeam(){
        ProgressDialog progress = new ProgressDialog(DetailTeamInfoActivity.this);
        progress.setMessage("Request Join...");
        progress.show();
        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiServices(sess.getString("token")).personnelLeaveTeam(sess.getString("id_user"));
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            String desc = response.body().getDesc();
                            Toast.makeText(DetailTeamInfoActivity.this, desc, Toast.LENGTH_SHORT).show();
                            finish();

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(DetailTeamInfoActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(DetailTeamInfoActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(DetailTeamInfoActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(DetailTeamInfoActivity.this, "Failed Request Join team", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(DetailTeamInfoActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}