package com.circle.circle_games.main;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.circle.circle_games.main.adapter.ListNewsAdapter;
import com.circle.circle_games.retrofit.model.GetListNewsResponseModel;
import com.circle.circle_games.utility.SessionUser;
import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.main.adapter.ListNewsAdapter;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.GetListNewsResponseModel;
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

public class NewsActivity extends AppCompatActivity {

    @BindView(R.id.recycler_news)
    RecyclerView rvNews;
    @BindView(R.id.search_bar_news)
    EditText etSearchBarNews;
    @BindView(R.id.btn_search)
    ImageView btnSearch;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.shimmer_load)
    ShimmerFrameLayout shimmerLoad;
    private String judulNews = "";
    private SessionUser sess;
    private GlobalMethod globalMethod;
    List<GetListNewsResponseModel.Data> listNews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_news);
        ButterKnife.bind(this);
        sess = new SessionUser(this);
        globalMethod = new GlobalMethod();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        getListNews(sess.getString("id_user"),judulNews,"0");
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListNews(sess.getString("id_user"),etSearchBarNews.getText().toString(),"0");
            }
        });
        etSearchBarNews.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getListNews(sess.getString("id_user"),etSearchBarNews.getText().toString(),"0");
                    return true;
                }
                return false;
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    private void getListNews(String userId, String search, String page ){
        globalMethod.setShimmerLinearLayout(true,shimmerLoad,llContent);
        try {
            Call<GetListNewsResponseModel> req = RetrofitConfig.getApiServices("").getListNews(userId, search, page);
            req.enqueue(new Callback<GetListNewsResponseModel>() {
                @Override
                public void onResponse(Call<GetListNewsResponseModel> call, Response<GetListNewsResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            listNews = response.body().getData();

                            rvNews.setAdapter(new ListNewsAdapter(NewsActivity.this,listNews));
                            rvNews.setLayoutManager(new LinearLayoutManager(NewsActivity.this));
                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(NewsActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.body().getCode().equals("05")){
                        String desc = response.body().getDesc();
                        Toast.makeText(NewsActivity.this, desc, Toast.LENGTH_SHORT).show();
                        globalMethod.setShimmerLinearLayout(false,shimmerLoad,llContent);
                        sess.clearSess();
                        Intent i = new Intent(NewsActivity.this, LoginActivity.class);
                        startActivity(i);
                        NewsActivity.this.finish();
                    }  else {
                        String desc = response.body().getDesc();
                        Toast.makeText(NewsActivity.this, desc, Toast.LENGTH_SHORT).show();
                        globalMethod.setShimmerLinearLayout(false,shimmerLoad,llContent);
                    }
                    globalMethod.setShimmerLinearLayout(false,shimmerLoad,llContent);
                }

                @Override
                public void onFailure(Call<GetListNewsResponseModel> call, Throwable t) {
                    Toast.makeText(NewsActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    globalMethod.setShimmerLinearLayout(false,shimmerLoad,llContent);

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}