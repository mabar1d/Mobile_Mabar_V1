package com.circle.circle_games.team;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.circle.circle_games.main.adapter.ListTeamTournamentAdapter;
import com.circle.circle_games.retrofit.model.GetListTeamTournamentResponseModel;
import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.main.adapter.ListPersonReqJoinTeamAdapter;
import com.circle.circle_games.main.adapter.ListTeamTournamentAdapter;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.GetListRequestJoinTeamResponseModel;
import com.circle.circle_games.retrofit.model.GetListTeamTournamentResponseModel;
import com.circle.circle_games.retrofit.model.SuccessResponseDefaultModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageTeamActivity extends AppCompatActivity {

    @BindView(R.id.recycler_person_req)
    RecyclerView rvPersonReqJoin;
    @BindView(R.id.recycler_tournament)
    RecyclerView rvTournament;
    @BindView(R.id.btn_back)
    ImageView btnBack;

    private ListPersonReqJoinTeamAdapter listPersonAdapter;
    private ListTeamTournamentAdapter listTeamTournamentAdapter;

    private GlobalMethod gm;
    private SessionUser sess;

    private String idTeam = "";
    private String memberName = "";

    List<GetListRequestJoinTeamResponseModel.Data> listPerson = new ArrayList<>();
    List<GetListTeamTournamentResponseModel.Data> listTournament = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_team);
        ButterKnife.bind(this);
        Bundle b = getIntent().getExtras();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if(b != null) {
            idTeam = b.getString("id_team");
        }

        gm = new GlobalMethod();
        sess = new SessionUser(this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getListPerson();
        getListTeamTournament();

    }

    private void getListPerson(){

        gm.showLoadingDialog(ManageTeamActivity.this);
        try {
            Call<GetListRequestJoinTeamResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getListReqJoinTeam(sess.getString("id_user"), idTeam);
            req.enqueue(new Callback<GetListRequestJoinTeamResponseModel>() {
                @Override
                public void onResponse(Call<GetListRequestJoinTeamResponseModel> call, Response<GetListRequestJoinTeamResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            listPerson.clear();
                            listPerson = response.body().getData();

                            listPersonAdapter = new ListPersonReqJoinTeamAdapter(ManageTeamActivity.this, listPerson, new ListPersonReqJoinTeamAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(GetListRequestJoinTeamResponseModel.Data item, int position) {
                                    listPerson.set(position, item);

                                    memberName = item.getUserRequestName();
                                    gm.showDialogConfirmation(ManageTeamActivity.this, "Answer Request?", "Add " + memberName +" to your team?", "Accept", "Decline", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String answer = "1";
                                            gm.dismissDialogConfirmation();
                                            answerReqJoin(answer,item.getUserRequestId());

                                        }
                                    }, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String answer = "0";
                                            gm.dismissDialogConfirmation();
                                            answerReqJoin(answer,item.getUserRequestId());
                                        }
                                    });

                                }
                            });

                            rvPersonReqJoin.setLayoutManager(new GridLayoutManager(ManageTeamActivity.this,2));
                            rvPersonReqJoin.setAdapter(listPersonAdapter);
                            listPersonAdapter.notifyDataSetChanged();


                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(ManageTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            gm.dismissLoadingDialog();
                            sess.clearSess();
                            Intent i = new Intent(ManageTeamActivity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(ManageTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ManageTeamActivity.this, "Failed Request List Person", Toast.LENGTH_SHORT).show();
                        gm.dismissLoadingDialog();
                    }
                    gm.dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<GetListRequestJoinTeamResponseModel> call, Throwable t) {
                    Toast.makeText(ManageTeamActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    gm.dismissLoadingDialog();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    //1 yes, 0 no
    private void answerReqJoin(String answer,int idRequested){

        gm.showLoadingDialog(ManageTeamActivity.this);
        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiServices(sess.getString("token")).answerReqJoinTeam(sess.getString("id_user"), idRequested,answer);
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            String desc = response.body().getDesc();
                            Toast.makeText(ManageTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            //getListPerson();
                            recreate();

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(ManageTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            gm.dismissLoadingDialog();
                            sess.clearSess();
                            Intent i = new Intent(ManageTeamActivity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(ManageTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ManageTeamActivity.this, "Failed Answer", Toast.LENGTH_SHORT).show();
                        gm.dismissLoadingDialog();
                    }
                    gm.dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    Toast.makeText(ManageTeamActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    gm.dismissLoadingDialog();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void getListTeamTournament(){

        gm.showLoadingDialog(ManageTeamActivity.this);
        try {
            Call<GetListTeamTournamentResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getListTeamTournamentforLeader(sess.getString("id_user"), idTeam);
            req.enqueue(new Callback<GetListTeamTournamentResponseModel>() {
                @Override
                public void onResponse(Call<GetListTeamTournamentResponseModel> call, Response<GetListTeamTournamentResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            listTournament.clear();
                            listTournament = response.body().getData();

                            listTeamTournamentAdapter = new ListTeamTournamentAdapter(ManageTeamActivity.this, listTournament);

                            rvTournament.setLayoutManager(new GridLayoutManager(ManageTeamActivity.this,2));
                            rvTournament.setAdapter(listTeamTournamentAdapter);


                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(ManageTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            gm.dismissLoadingDialog();
                            sess.clearSess();
                            Intent i = new Intent(ManageTeamActivity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(ManageTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ManageTeamActivity.this, "Failed Request List Tournament", Toast.LENGTH_SHORT).show();
                        gm.dismissLoadingDialog();
                    }
                    gm.dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<GetListTeamTournamentResponseModel> call, Throwable t) {
                    Toast.makeText(ManageTeamActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    gm.dismissLoadingDialog();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }


}