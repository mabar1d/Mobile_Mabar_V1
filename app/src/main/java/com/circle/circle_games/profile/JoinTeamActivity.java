package com.circle.circle_games.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.circle.circle_games.main.adapter.ListTeamAdapter;
import com.circle.circle_games.utility.SessionUser;
import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.main.adapter.ListTeamAdapter;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.GetListTeamResponseModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinTeamActivity extends AppCompatActivity {

    @BindView(R.id.search_bar_team)
    EditText searchTeam;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.recycler_team)
    RecyclerView rvTeam;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.shimmer_load)
    ShimmerFrameLayout shimmerLoad;
    @BindView(R.id.btn_back)
    ImageView btnBack;

    private GlobalMethod gm;
    private SessionUser sess;

    private ListTeamAdapter listTeamAdapter;
    List<GetListTeamResponseModel.Data> listTeam = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_team);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        gm = new GlobalMethod();
        sess = new SessionUser(this);

        getListTeam(sess.getString("id_user"),"","");

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListTeam(sess.getString("id_user"),searchTeam.getText().toString(),"0");
            }
        });
        searchTeam.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getListTeam(sess.getString("id_user"),searchTeam.getText().toString(),"0");
                    return true;
                }
                return false;
            }
        });

    }

    private void getListTeam(String userId,String search,String page){

        gm.setShimmerLinearLayout(true,shimmerLoad,llContent);
        try {
            Call<GetListTeamResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getListTeam(userId, search, page);
            req.enqueue(new Callback<GetListTeamResponseModel>() {
                @Override
                public void onResponse(Call<GetListTeamResponseModel> call, Response<GetListTeamResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            listTeam.clear();
                            listTeam = response.body().getData();

                            listTeamAdapter = new ListTeamAdapter(JoinTeamActivity.this,listTeam);
                            rvTeam.setLayoutManager(new GridLayoutManager(JoinTeamActivity.this,2,GridLayoutManager.VERTICAL,false));
                            rvTeam.setAdapter(listTeamAdapter);
                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(JoinTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                            sess.clearSess();
                            Intent i = new Intent(JoinTeamActivity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(JoinTeamActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(JoinTeamActivity.this, "Failed Request List Teams", Toast.LENGTH_SHORT).show();
                    }
                    gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
                }

                @Override
                public void onFailure(Call<GetListTeamResponseModel> call, Throwable t) {
                    Toast.makeText(JoinTeamActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    gm.setShimmerLinearLayout(false,shimmerLoad,llContent);

                }


            });
        }catch (Exception e){
            e.printStackTrace();
            gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
        }


    }
}