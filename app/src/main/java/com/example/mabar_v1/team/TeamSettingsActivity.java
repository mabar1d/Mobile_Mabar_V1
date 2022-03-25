package com.example.mabar_v1.team;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.mabar_v1.R;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.main.adapter.ListGameAdapter;
import com.example.mabar_v1.main.adapter.ListTeamAdapter;
import com.example.mabar_v1.retrofit.RetrofitConfig;
import com.example.mabar_v1.retrofit.model.GetListTournamentResponseModel;
import com.example.mabar_v1.retrofit.model.ListTeamResponseModel;
import com.example.mabar_v1.retrofit.model.ResponseListGame;
import com.example.mabar_v1.splash.SplashScreen1;
import com.example.mabar_v1.utility.GlobalMethod;
import com.example.mabar_v1.utility.SessionUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamSettingsActivity extends AppCompatActivity {

    @BindView(R.id.search_bar_team)
    EditText searchTeam;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.recycler_team)
    RecyclerView rvTeam;
    @BindView(R.id.btn_create_team)
    TextView btnCreateTeam;
    @BindView(R.id.btn_edit_team)
    TextView btnEditTeam;
    @BindView(R.id.btn_manage_team)
    TextView btnManageTeam;

    private GlobalMethod gm;
    private SessionUser sess;

    private ListTeamAdapter listTeamAdapter;
    List<ListTeamResponseModel.Data> listTeam = new ArrayList<>();

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

        getListGame(sess.getString("id_user"),"","");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListGame(sess.getString("id_user"),searchTeam.getText().toString(),"0");
            }
        });
        searchTeam.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getListGame(sess.getString("id_user"),searchTeam.getText().toString(),"0");
                    return true;
                }
                return false;
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
                getListGame(sess.getString("id_user"),searchTeam.getText().toString(),"0");
            }
        });

        btnManageTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListGame(sess.getString("id_user"),searchTeam.getText().toString(),"0");
            }
        });

    }

    private void getListGame(String userId,String search,String page){

        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.show();
        try {
            Call<ListTeamResponseModel> req = RetrofitConfig.getApiServices("").getListTeam(userId, search, page);
            req.enqueue(new Callback<ListTeamResponseModel>() {
                @Override
                public void onResponse(Call<ListTeamResponseModel> call, Response<ListTeamResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            listTeam.clear();
                            listTeam = response.body().getData();

                            listTeamAdapter = new ListTeamAdapter(TeamSettingsActivity.this,listTeam);
                            rvTeam.setLayoutManager(new GridLayoutManager(TeamSettingsActivity.this,2,GridLayoutManager.HORIZONTAL,false));
                            rvTeam.setAdapter(listTeamAdapter);
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
                        Toast.makeText(TeamSettingsActivity.this, "Failed Request List Teams", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<ListTeamResponseModel> call, Throwable t) {
                    Toast.makeText(TeamSettingsActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    progress.dismiss();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}