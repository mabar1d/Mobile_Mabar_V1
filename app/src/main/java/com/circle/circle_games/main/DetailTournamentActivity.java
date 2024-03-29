package com.circle.circle_games.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.circle.circle_games.PaymentActivity;
import com.circle.circle_games.retrofit.model.GetTermsConditionResponseModel;
import com.circle.circle_games.utility.SessionUser;
import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.GetLinkTreeWebviewResponseModel;
import com.circle.circle_games.retrofit.model.ResponseGetInfoTournamentModel;
import com.circle.circle_games.retrofit.model.SuccessResponseDefaultModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.btn_fixture_tournament)
    Button btnFixtureTournament;
    @BindView(R.id.btn_table_tournament)
    Button btnTableTournament;
    @BindView(R.id.btn_back)
    ImageView btnBack;

    //Terms n Condition
    @BindView(R.id.ll_general_terms_condition)
    LinearLayout llGeneralTerms;
    @BindView(R.id.tv_general_terms)
    TextView tvGeneralTerms;
    @BindView(R.id.expand_general_terms)
    ImageView btnExpandGeneralTerms;

    @BindView(R.id.ll_tournament_terms_condition)
    LinearLayout llTournamentTerms;
    @BindView(R.id.tv_tournament_terms)
    TextView tvTournamentTerms;
    @BindView(R.id.expand_tournament_terms)
    ImageView btnExpandTournamentTerms;

    private String idTournament = "";
    private String usage = "";
    private String judulGame = "";
    private SessionUser sess;
    private GlobalMethod gm;
    private String fee = "";
    private String feeText = "";
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
            usage = b.getString("usage");
        }

        sess = new SessionUser(this);
        gm = new GlobalMethod();

        getInfoTournament(sess.getString("id_user"));

        if (!(usage == null)){
            btnRegister.setVisibility(View.GONE);
            btnFixtureTournament.setVisibility(View.VISIBLE);
            btnTableTournament.setVisibility(View.VISIBLE);
        }else {
            btnRegister.setVisibility(View.VISIBLE);
            btnFixtureTournament.setVisibility(View.GONE);
            btnTableTournament.setVisibility(View.GONE);
        }

        llTournamentTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvTournamentTerms.getVisibility() == View.VISIBLE){
                    tvTournamentTerms.setVisibility(View.GONE);
                }else {
                    tvTournamentTerms.setVisibility(View.VISIBLE);
                }
            }
        });
        btnExpandTournamentTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvTournamentTerms.getVisibility() == View.VISIBLE){
                    tvTournamentTerms.setVisibility(View.GONE);
                }else {
                    tvTournamentTerms.setVisibility(View.VISIBLE);
                }
            }
        });
        llGeneralTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvGeneralTerms.getVisibility() == View.VISIBLE){
                    tvGeneralTerms.setVisibility(View.GONE);
                }else {
                    tvGeneralTerms.setVisibility(View.VISIBLE);
                }
            }
        });
        btnExpandGeneralTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvGeneralTerms.getVisibility() == View.VISIBLE){
                    tvGeneralTerms.setVisibility(View.GONE);
                }else {
                    tvGeneralTerms.setVisibility(View.VISIBLE);
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gm.showDialogConfirmation(DetailTournamentActivity.this, "Register Tournament?", "Are you sure?",
                        "Register", "Cancel", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (fee.equalsIgnoreCase("Free")){
                                    registerTournament(sess.getString("id_user"),idTournament);
                                }else {
                                    Intent i = new Intent(DetailTournamentActivity.this, PaymentActivity.class);
                                    Bundle bun = new Bundle();
                                    bun.putString("id_tournament", idTournament);
                                    bun.putString("nama_tournament", tvJudulTourney.getText().toString());
                                    bun.putString("fee",feeText);
                                    i.putExtras(bun);
                                    startActivity(i);
                                }
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

        btnFixtureTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLinkTree(sess.getString("id_user"),idTournament);
            }
        });

        btnTableTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLinkTree(sess.getString("id_user"),idTournament);
            }
        });



    }

    private void getInfoTournament(String userId ){
        gm.showLoadingDialog(DetailTournamentActivity.this);
        try {
            Call<ResponseGetInfoTournamentModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getInfoTournament(userId, idTournament);
            req.enqueue(new Callback<ResponseGetInfoTournamentModel>() {
                @Override
                public void onResponse(Call<ResponseGetInfoTournamentModel> call, Response<ResponseGetInfoTournamentModel> response) {
                    gm.dismissLoadingDialog();
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            Glide.with(DetailTournamentActivity.this)
                                    .load(response.body().getData().getImage())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    //.skipMemoryCache(true)
                                    .into(ivTournament);
                            tvJudulTourney.setText(response.body().getData().getName());
                            tvTournamentTerms.setText(response.body().getData().getTermsCondition());
                            if (response.body().getData().getRating() != null){
                                tvRating.setText(response.body().getData().getRating());
                            }else {
                                tvRating.setText("-");
                            }

                            tvPrize.setText(gm.formattedRupiah(response.body().getData().getPrize()));
                            tvGame.setText(response.body().getData().getTitleGame());
                            tvCreatedBy.setText("Created By "+response.body().getData().getCreatedName());
                            tvStartDate.setText(response.body().getData().getStartDate());
                            tvEndDate.setText(response.body().getData().getEndDate());
                            tvRegStart.setText(response.body().getData().getRegisterDateStart());
                            tvRegEnd.setText(response.body().getData().getRegisterDateEnd());
                            tvNumParticipant.setText(response.body().getData().getNumberOfParticipants().toString()+" Slot");
                            tvDescription.setText(response.body().getData().getDetail());
                            fee = response.body().getData().getRegisterFee();
                            feeText = response.body().getData().getRegisterFee();

                            if (fee.equalsIgnoreCase("0")){
                                fee = "FREE";
                            }else {
                                fee = gm.formattedRupiah(fee);
                            }
                            btnRegister.setText("Register "+ "("+fee+")");

                            if (response.body().getData().getTeamInTournament() != null){
                                for (int i=0;i<response.body().getData().getTeamInTournament().size();i++){

                                }
                            }
                            mappingButton(response.body().getData().getRegisterDateEnd());

                            getTermsCondition(sess.getString("id_user"));


                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(DetailTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                            gm.dismissLoadingDialog();
                            sess.clearSess();
                            Intent i = new Intent(DetailTournamentActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(DetailTournamentActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String desc = response.body().getDesc();
                        Toast.makeText(DetailTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        gm.dismissLoadingDialog();
                    }
                    gm.dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<ResponseGetInfoTournamentModel> call, Throwable t) {
                    Toast.makeText(DetailTournamentActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    gm.dismissLoadingDialog();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getTermsCondition(String userId){
        gm.showLoadingDialog(DetailTournamentActivity.this);
        try {
            Call<GetTermsConditionResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getInfoGeneral(userId, "terms_condition");
            req.enqueue(new Callback<GetTermsConditionResponseModel>() {
                @Override
                public void onResponse(Call<GetTermsConditionResponseModel> call, Response<GetTermsConditionResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            tvGeneralTerms.setText(response.body().getData().getDesc());


                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(DetailTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                            gm.dismissLoadingDialog();
                            sess.clearSess();
                            Intent i = new Intent(DetailTournamentActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(DetailTournamentActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String desc = response.body().getDesc();
                        Toast.makeText(DetailTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        gm.dismissLoadingDialog();
                    }
                    gm.dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<GetTermsConditionResponseModel> call, Throwable t) {
                    Toast.makeText(DetailTournamentActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    gm.dismissLoadingDialog();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getLinkTree(String userId,String idTournament){
        gm.showLoadingDialog(DetailTournamentActivity.this);
        try {
            Call<GetLinkTreeWebviewResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getLinkTournamentTreeWeb(userId, idTournament);
            req.enqueue(new Callback<GetLinkTreeWebviewResponseModel>() {
                @Override
                public void onResponse(Call<GetLinkTreeWebviewResponseModel> call, Response<GetLinkTreeWebviewResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            String url = response.body().getData().getUrl();
                            Intent i = new Intent(DetailTournamentActivity.this, TournamentGraphWebViewActivity.class);
                            Bundle bun = new Bundle();
                            bun.putString("url", url);
                            i.putExtras(bun);
                            startActivity(i);

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(DetailTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                            gm.dismissLoadingDialog();
                            sess.clearSess();
                            Intent i = new Intent(DetailTournamentActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(DetailTournamentActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String desc = response.body().getDesc();
                        Toast.makeText(DetailTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        gm.dismissLoadingDialog();
                    }
                    gm.dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<GetLinkTreeWebviewResponseModel> call, Throwable t) {
                    Toast.makeText(DetailTournamentActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    gm.dismissLoadingDialog();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void registerTournament(String userId,String idTournament){
        gm.showLoadingDialog(DetailTournamentActivity.this);
        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiServices(sess.getString("token")).registerTournament(userId, idTournament);
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            String notif = response.body().getDesc();
                            Toast.makeText(DetailTournamentActivity.this, notif, Toast.LENGTH_SHORT).show();
                            finish();
                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(DetailTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                            gm.dismissLoadingDialog();
                            sess.clearSess();
                            Intent i = new Intent(DetailTournamentActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(DetailTournamentActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String desc = response.body().getDesc();
                        Toast.makeText(DetailTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        gm.dismissLoadingDialog();
                    }
                    gm.dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    Toast.makeText(DetailTournamentActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    gm.dismissLoadingDialog();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void mappingButton(String endRegDate){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            final Date endReg = sdf.parse(endRegDate);
            final Date now = new Date();

            if (usage == null){
                if (now.after(endReg)) {
                    // kadaluarsa
                    btnRegister.setVisibility(View.GONE);
                    btnFixtureTournament.setVisibility(View.VISIBLE);
                    btnTableTournament.setVisibility(View.VISIBLE);
                } else if(now.before(endReg)) {
                    // belum kadaluarsa
                    btnRegister.setVisibility(View.VISIBLE);
                    btnFixtureTournament.setVisibility(View.GONE);
                    btnTableTournament.setVisibility(View.GONE);
                } else {
                    // sama
                    btnRegister.setVisibility(View.VISIBLE);
                    btnFixtureTournament.setVisibility(View.GONE);
                    btnTableTournament.setVisibility(View.GONE);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}