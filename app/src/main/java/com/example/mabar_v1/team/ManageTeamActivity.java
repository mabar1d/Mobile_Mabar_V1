package com.example.mabar_v1.team;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mabar_v1.R;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.main.adapter.ListPersonAdapter;
import com.example.mabar_v1.main.adapter.ListPersonAddedAdapter;
import com.example.mabar_v1.main.adapter.ListPersonReqJoinTeamAdapter;
import com.example.mabar_v1.main.adapter.ListTeamTournamentAdapter;
import com.example.mabar_v1.retrofit.RetrofitConfig;
import com.example.mabar_v1.retrofit.model.GetListRequestJoinTeamResponseModel;
import com.example.mabar_v1.retrofit.model.GetListTeamTournamentResponseModel;
import com.example.mabar_v1.retrofit.model.ListPersonnelResponseModel;
import com.example.mabar_v1.retrofit.model.SuccessResponseDefaultModel;
import com.example.mabar_v1.utility.GlobalMethod;
import com.example.mabar_v1.utility.SessionUser;

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

        getListPerson();
        getListTeamTournament();

    }

    private void getListPerson(){

        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.show();
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
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(ManageTeamActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(ManageTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ManageTeamActivity.this, "Failed Request List Person", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<GetListRequestJoinTeamResponseModel> call, Throwable t) {
                    Toast.makeText(ManageTeamActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    progress.dismiss();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    //1 yes, 0 no
    private void answerReqJoin(String answer,int idRequested){

        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.show();
        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiServices(sess.getString("token")).answerReqJoinTeam(sess.getString("id_user"), idRequested,answer);
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            String desc = response.body().getDesc();
                            Toast.makeText(ManageTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            getListPerson();

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(ManageTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(ManageTeamActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(ManageTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ManageTeamActivity.this, "Failed Answer", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    Toast.makeText(ManageTeamActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    progress.dismiss();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void getListTeamTournament(){

        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.show();
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
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(ManageTeamActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(ManageTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(ManageTeamActivity.this, "Failed Request List Tournament", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<GetListTeamTournamentResponseModel> call, Throwable t) {
                    Toast.makeText(ManageTeamActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    progress.dismiss();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }


}