package com.example.mabar_v1.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mabar_v1.R;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.main.adapter.ListTournamentAdapter;
import com.example.mabar_v1.retrofit.RetrofitConfig;
import com.example.mabar_v1.retrofit.model.GetListTournamentResponseModel;
import com.example.mabar_v1.utility.SessionUser;

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
    private String idGame = "";
    private String judulGame = "";
    private JSONArray fltGame = new JSONArray();
    private SessionUser sess;
    List<GetListTournamentResponseModel.Data> listTournament = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_search_tournament);
        ButterKnife.bind(this);
        sess = new SessionUser(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Bundle b = getIntent().getExtras();
        idGame = b.getString("id_game");
        judulGame = b.getString("judul_game");

        fltGame.put(idGame);

        getListTournament(sess.getString("id_user"),"","0",fltGame);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListTournament(sess.getString("id_user"),etSearchBarTournament.getText().toString(),"0",fltGame);
            }
        });
        etSearchBarTournament.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getListTournament(sess.getString("id_user"),etSearchBarTournament.getText().toString(),"0",fltGame);
                    return true;
                }
                return false;
            }
        });

    }
    private void getListTournament(String userId, String search, String page, JSONArray filterGame ){
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.show();
        try {
            Call<GetListTournamentResponseModel> req = RetrofitConfig.getApiServices("").getListTournament(userId, search, page,filterGame);
            req.enqueue(new Callback<GetListTournamentResponseModel>() {
                @Override
                public void onResponse(Call<GetListTournamentResponseModel> call, Response<GetListTournamentResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            listTournament = response.body().getData();

                            rvTournament.setAdapter(new ListTournamentAdapter(GeneralSearchTournamentActivity.this,listTournament));
                            rvTournament.setLayoutManager(new GridLayoutManager(GeneralSearchTournamentActivity.this,2));
                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(GeneralSearchTournamentActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.body().getCode().equals("05")){
                        String desc = response.body().getDesc();
                        Toast.makeText(GeneralSearchTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                        sess.clearSess();
                        Intent i = new Intent(GeneralSearchTournamentActivity.this, LoginActivity.class);
                        startActivity(i);
                        GeneralSearchTournamentActivity.this.finish();
                    }  else {
                        String desc = response.body().getDesc();
                        Toast.makeText(GeneralSearchTournamentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<GetListTournamentResponseModel> call, Throwable t) {
                    Toast.makeText(GeneralSearchTournamentActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    progress.dismiss();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}