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

import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.main.adapter.ListVideosAdapter;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.GetListNewsResponseModel;
import com.circle.circle_games.retrofit.model.GetListVideosResponseModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideosActivity extends AppCompatActivity implements AddLifecycleCallbackListener {

    @BindView(R.id.recycler_videos)
    RecyclerView rvVideos;
    @BindView(R.id.search_bar_videos)
    EditText etSearchBarVideos;
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
    List<GetListVideosResponseModel.Data> listNews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);
        ButterKnife.bind(this);
        sess = new SessionUser(this);
        globalMethod = new GlobalMethod();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        getListVideos(sess.getString("id_user"),judulNews,"0");
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListVideos(sess.getString("id_user"),etSearchBarVideos.getText().toString(),"0");
            }
        });
        etSearchBarVideos.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    getListVideos(sess.getString("id_user"),etSearchBarVideos.getText().toString(),"0");
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
    private void getListVideos(String userId, String search, String page ){
        globalMethod.setShimmerLinearLayout(true,shimmerLoad,llContent);
        try {
            Call<GetListVideosResponseModel> req = RetrofitConfig.getApiServices("").getListVideo(userId, search, page);
            req.enqueue(new Callback<GetListVideosResponseModel>() {
                @Override
                public void onResponse(Call<GetListVideosResponseModel> call, Response<GetListVideosResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            listNews = response.body().getData();

                            rvVideos.setAdapter(new ListVideosAdapter(VideosActivity.this,listNews));
                            rvVideos.setLayoutManager(new LinearLayoutManager(VideosActivity.this));
                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(VideosActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.body().getCode().equals("05")){
                        String desc = response.body().getDesc();
                        Toast.makeText(VideosActivity.this, desc, Toast.LENGTH_SHORT).show();
                        globalMethod.setShimmerLinearLayout(false,shimmerLoad,llContent);
                        sess.clearSess();
                        Intent i = new Intent(VideosActivity.this, LoginActivity.class);
                        startActivity(i);
                        VideosActivity.this.finish();
                    }  else {
                        String desc = response.body().getDesc();
                        Toast.makeText(VideosActivity.this, desc, Toast.LENGTH_SHORT).show();
                        globalMethod.setShimmerLinearLayout(false,shimmerLoad,llContent);
                    }
                    globalMethod.setShimmerLinearLayout(false,shimmerLoad,llContent);
                }

                @Override
                public void onFailure(Call<GetListVideosResponseModel> call, Throwable t) {
                    Toast.makeText(VideosActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    globalMethod.setShimmerLinearLayout(false,shimmerLoad,llContent);

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void addLifeCycleCallBack(YouTubePlayerView youTubePlayerView) {

    }
}

