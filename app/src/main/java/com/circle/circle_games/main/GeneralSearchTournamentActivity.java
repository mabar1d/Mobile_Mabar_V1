package com.circle.circle_games.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.main.adapter.ListTournamentAdapter;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.GetListTournamentResponseModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeneralSearchTournamentActivity extends AppCompatActivity {

    @BindView(R.id.recycler_my_tournaments)
    RecyclerView rvTournament;
    @BindView(R.id.search_bar_tournament)
    EditText etSearchBarTournament;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    private String idGame = "";
    private String judulGame = "";
    private JSONArray fltGame = new JSONArray();
    private SessionUser sess;
    List<GetListTournamentResponseModel.Data> listTournament = new ArrayList<>();
    LinearLayoutManager layoutManager;
    GlobalMethod gm;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = 0;
    private int lastVisibleItemPosition = 0;
    private int lastVisibleItemOffset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_search_tournament);
        ButterKnife.bind(this);
        sess = new SessionUser(this);
        gm = new GlobalMethod();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bundle b = getIntent().getExtras();
        if (b.getString("id_game") != null){
            idGame = b.getString("id_game");
        }
        if (b.getString("judul_game") != null){
            judulGame = b.getString("judul_game");
        }


        if (idGame != null && !idGame.equalsIgnoreCase("")){
            fltGame.put(idGame);
        }


        if (judulGame != null){
            etSearchBarTournament.setText(judulGame);
        }else {
            judulGame = "";
        }
        layoutManager = new LinearLayoutManager(GeneralSearchTournamentActivity.this);

        listenerRecyclerView();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getListTournament(sess.getString("id_user"),judulGame,String.valueOf(currentPage),fltGame);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListTournament(sess.getString("id_user"),judulGame,String.valueOf(currentPage),fltGame);
            }
        });
        etSearchBarTournament.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getListTournament(sess.getString("id_user"),judulGame,String.valueOf(currentPage),fltGame);
                    return true;
                }
                return false;
            }
        });

    }

    private void listenerRecyclerView(){
        rvTournament.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Save the current scroll position
                lastVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                View lastVisibleItem = layoutManager.findViewByPosition(lastVisibleItemPosition);
                if (lastVisibleItem != null) {
                    lastVisibleItemOffset = lastVisibleItem.getTop();
                }

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + lastVisibleItemPosition) >= totalItemCount
                            && lastVisibleItemPosition >= 0) {
                        // Set the flag to prevent multiple data loading calls
                        isLoading = true;
                        getListTournament(sess.getString("id_user"),judulGame,String.valueOf(currentPage),fltGame);
                    }
                }
            }
        });
    }
    private void getListTournament(String userId, String search, String page, JSONArray filterGame ){
        gm.showLoadingDialog(GeneralSearchTournamentActivity.this);
        try {
            Call<GetListTournamentResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getListTournament(userId, search, page,filterGame);
            req.enqueue(new Callback<GetListTournamentResponseModel>() {
                @Override
                public void onResponse(Call<GetListTournamentResponseModel> call, Response<GetListTournamentResponseModel> response) {
                    isLoading = false;
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            currentPage++;
                            listTournament = response.body().getData();
                            rvTournament.setAdapter(new ListTournamentAdapter(GeneralSearchTournamentActivity.this,listTournament,"menu"));
                            rvTournament.setLayoutManager(layoutManager);

                            // Restore the scroll position
                            layoutManager.scrollToPositionWithOffset(lastVisibleItemPosition, lastVisibleItemOffset);

                        } else if (response.body().getCode().equals("02")) {
                            isLastPage = true;
                            String notif = response.body().getDesc();
                            Toast.makeText(GeneralSearchTournamentActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(GeneralSearchTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                            gm.dismissLoadingDialog();
                            sess.clearSess();
                            Intent i = new Intent(GeneralSearchTournamentActivity.this, LoginActivity.class);
                            startActivity(i);
                            GeneralSearchTournamentActivity.this.finish();
                        } else {
                            String notif = response.body().getDesc();
                            Toast.makeText(GeneralSearchTournamentActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String desc = response.body().getDesc();
                        Toast.makeText(GeneralSearchTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        gm.dismissLoadingDialog();
                    }
                    gm.dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<GetListTournamentResponseModel> call, Throwable t) {
                    Toast.makeText(GeneralSearchTournamentActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    gm.dismissLoadingDialog();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}