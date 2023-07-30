package com.circle.circle_games.team;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.main.adapter.ListHostTournamentAdapter;
import com.circle.circle_games.main.adapter.ListMemberTeamTournamentAdapter;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.GetListTournamentResponseModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MemberTeamListTournamentActivity extends AppCompatActivity {

    @BindView(R.id.recycler_my_tournaments)
    RecyclerView rvTournament;
    @BindView(R.id.search_bar_tournament)
    EditText etSearchBarTournament;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.shimmer_load)
    ShimmerFrameLayout shimmerLoad;
    @BindView(R.id.btn_back)
    ImageView btnBack;

    private String teamId = "";
    private SessionUser sess;
    private GlobalMethod gm;
    List<GetListTournamentResponseModel.Data> listTournament = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_team_list_tournament);
        ButterKnife.bind(this);

        sess = new SessionUser(this);
        gm = new GlobalMethod();

        Bundle b = getIntent().getExtras();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if(b != null) {
            teamId = b.getString("id_team");
        }


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListTeamTournamentForMember(sess.getString("id_user"),teamId,etSearchBarTournament.getText().toString(),"0");
            }
        });
        etSearchBarTournament.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getListTeamTournamentForMember(sess.getString("id_user"),teamId,etSearchBarTournament.getText().toString(),"0");
                    return true;
                }
                return false;
            }
        });

    }
    private void getListTeamTournamentForMember(String userId, String teamId , String search, String page){
        gm.setShimmerLinearLayout(true,shimmerLoad,llContent);
        try {
            Call<GetListTournamentResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getListTeamTournamentforMember(userId, teamId,search,page);
            req.enqueue(new Callback<GetListTournamentResponseModel>() {
                @Override
                public void onResponse(Call<GetListTournamentResponseModel> call, Response<GetListTournamentResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            listTournament = response.body().getData();

                            rvTournament.setAdapter(new ListMemberTeamTournamentAdapter(MemberTeamListTournamentActivity.this,listTournament));
                            rvTournament.setLayoutManager(new GridLayoutManager(MemberTeamListTournamentActivity.this,2));
                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(MemberTeamListTournamentActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.body().getCode().equals("05")){
                        String desc = response.body().getDesc();
                        Toast.makeText(MemberTeamListTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        sess.clearSess();
                        Intent i = new Intent(MemberTeamListTournamentActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }  else {
                        String desc = response.body().getDesc();
                        Toast.makeText(MemberTeamListTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                    }
                    gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
                }

                @Override
                public void onFailure(Call<GetListTournamentResponseModel> call, Throwable t) {
                    Toast.makeText(MemberTeamListTournamentActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    gm.setShimmerLinearLayout(false,shimmerLoad,llContent);

                }


            });
        }catch (Exception e){
            e.printStackTrace();
            gm.setShimmerLinearLayout(true,shimmerLoad,llContent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getListTeamTournamentForMember(sess.getString("id_user"),teamId,"","0");
    }

}