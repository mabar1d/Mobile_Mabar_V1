package com.example.mabar_v1.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mabar_v1.R;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.main.adapter.ListTournamentAdapter;
import com.example.mabar_v1.retrofit.RetrofitConfig;
import com.example.mabar_v1.retrofit.model.GetListTournamentResponseModel;
import com.example.mabar_v1.retrofit.model.ResponseGetInfoTournamentModel;
import com.example.mabar_v1.retrofit.model.SuccessResponseDefaultModel;
import com.example.mabar_v1.utility.GlobalMethod;
import com.example.mabar_v1.utility.SessionUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailTournamentActivity extends AppCompatActivity {

    @BindView(R.id.image_tournament)
    ImageView ivTournament;
    @BindView(R.id.tv_judul_tourney)
    TextView tvJudulTourney;
    @BindView(R.id.rating_tourney)
    TextView tvRating;
    @BindView(R.id.tv_prize)
    TextView tvPrize;
    @BindView(R.id.tv_game)
    TextView tvGame;
    @BindView(R.id.tv_created_by)
    TextView tvCreatedBy;
    @BindView(R.id.tv_start_date)
    TextView tvStartDate;
    @BindView(R.id.tv_end_date)
    TextView tvEndDate;
    @BindView(R.id.tv_reg_start)
    TextView tvRegStart;
    @BindView(R.id.tv_reg_end)
    TextView tvRegEnd;
    @BindView(R.id.num_participant)
    TextView tvNumParticipant;
    @BindView(R.id.description)
    TextView tvDescription;
    @BindView(R.id.btn_register_tournament)
    Button btnRegister;

    private String idTournament = "";
    private String judulGame = "";
    private SessionUser sess;
    private GlobalMethod gm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tournament);
        ButterKnife.bind(this);
        Bundle b = getIntent().getExtras();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if(b != null) {
            idTournament = b.getString("id_tournament");
            judulGame = b.getString("judul_game");
        }

        sess = new SessionUser(this);
        gm = new GlobalMethod();

        getInfoTournament(sess.getString("id_user"),idTournament);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gm.showDialogConfirmation(DetailTournamentActivity.this, "Register Tournament?", "Are you sure?",
                        "Register", "Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                registerTournament(sess.getString("id_user"),idTournament);
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

    private void getInfoTournament(String userId,String idTournament){
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.show();
        try {
            Call<ResponseGetInfoTournamentModel> req = RetrofitConfig.getApiServices("").getInfoTournament(userId, idTournament);
            req.enqueue(new Callback<ResponseGetInfoTournamentModel>() {
                @Override
                public void onResponse(Call<ResponseGetInfoTournamentModel> call, Response<ResponseGetInfoTournamentModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            Glide.with(DetailTournamentActivity.this)
                                    .load(response.body().getData().getImage())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    //.skipMemoryCache(true)
                                    .into(ivTournament);
                            tvJudulTourney.setText(response.body().getData().getName());
                            if (response.body().getData().getRating() != null){
                                tvRating.setText(response.body().getData().getRating());
                            }else {
                                tvRating.setText("-");
                            }

                            tvPrize.setText("Rp. "+response.body().getData().getPrize());
                            tvGame.setText(response.body().getData().getTitleGame());
                            tvCreatedBy.setText("Created By "+response.body().getData().getCreatedName());
                            tvStartDate.setText(response.body().getData().getStartDate());
                            tvEndDate.setText(response.body().getData().getEndDate());
                            tvRegStart.setText(response.body().getData().getRegisterDateStart());
                            tvRegEnd.setText(response.body().getData().getRegisterDateEnd());
                            tvNumParticipant.setText(response.body().getData().getNumberOfParticipants().toString()+" Slot");
                            tvDescription.setText(response.body().getData().getDetail());
                            String fee = response.body().getData().getRegister_fee();

                            if (fee.equalsIgnoreCase("0")){
                                fee = "FREE";
                            }else {
                                fee = "Rp. "+fee;
                            }
                            btnRegister.setText("Register "+ "("+fee+")");


                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(DetailTournamentActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.body().getCode().equals("05")){
                        String desc = response.body().getDesc();
                        Toast.makeText(DetailTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                        sess.clearSess();
                        Intent i = new Intent(DetailTournamentActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }  else {
                        String desc = response.body().getDesc();
                        Toast.makeText(DetailTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseGetInfoTournamentModel> call, Throwable t) {
                    Toast.makeText(DetailTournamentActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    progress.dismiss();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void registerTournament(String userId,String idTournament){
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.show();
        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiServices("").registerTournament(userId, idTournament);
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            String notif = response.body().getDesc();
                            Toast.makeText(DetailTournamentActivity.this, notif, Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(DetailTournamentActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.body().getCode().equals("05")){
                        String desc = response.body().getDesc();
                        Toast.makeText(DetailTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                        sess.clearSess();
                        Intent i = new Intent(DetailTournamentActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }  else {
                        String desc = response.body().getDesc();
                        Toast.makeText(DetailTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    Toast.makeText(DetailTournamentActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    progress.dismiss();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}