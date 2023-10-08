package com.circle.circle_games.main;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.circle.circle_games.retrofit.model.GetInfoNewsResponseModel;
import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.GetInfoNewsResponseModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.facebook.shimmer.ShimmerFrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailNewsActivity extends AppCompatActivity {

    @BindView(R.id.image_news)
    ImageView ivNews;
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_share)
    ImageView btnShare;
    @BindView(R.id.tv_judul_news)
    TextView tvJudulNews;
    @BindView(R.id.tv_authors)
    TextView tvAuthors;
    @BindView(R.id.tv_tgl_news)
    TextView tvTglNews;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.shimmer_load)
    ShimmerFrameLayout shimmerLoad;
    @BindView(R.id.tv_diff_date)
    TextView tvDiffDate;

    private String idNews = "";
    private String urlImage = "";
    private String fileUrlShare = "";
    private String titleNews = "";
    private SessionUser sess;
    private GlobalMethod gm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_news);
        ButterKnife.bind(this);
        Bundle b = getIntent().getExtras();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if(b != null) {
            idNews = b.getString("id_news");
        }

        sess = new SessionUser(this);
        gm = new GlobalMethod();

        getInfoNews(sess.getString("id_user"),idNews);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Body = "I Have a News For You! "+ "'" + titleNews +  "'" +"\n"
                        + "Read the News Here: " + fileUrlShare + "\n" + "Stay tuned Only On Circle Games ID !";

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/html");// You Can set source type here like video, image text, etc.
                shareIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Circle Games");
                shareIntent.putExtra(Intent.EXTRA_TEXT, Body);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "Share File Using!"));

            }
        });



    }

    private void getInfoNews(String userId,String idTournament){

        gm.setShimmerLinearLayout(true,shimmerLoad,llContent);

        try {
            Call<GetInfoNewsResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getInfoNews(userId, idTournament);
            req.enqueue(new Callback<GetInfoNewsResponseModel>() {
                @Override
                public void onResponse(Call<GetInfoNewsResponseModel> call, Response<GetInfoNewsResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            CircularProgressDrawable cp = new CircularProgressDrawable(DetailNewsActivity.this);
                            cp.setStrokeWidth(5f);
                            //cp.setBackgroundColor(R.color.material_grey_300);
                            cp.setColorSchemeColors(R.color.primary_color_black, R.color.material_grey_800, R.color.material_grey_900);
                            cp.setCenterRadius(30f);
                            cp.start();

                            Glide.with(DetailNewsActivity.this)
                                    .load(response.body().getData().getNewsImageUrl())
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .placeholder(cp)
                                    //.skipMemoryCache(true)
                                    .into(ivNews);
                            tvJudulNews.setText(response.body().getData().getTitle());
                            tvAuthors.setText(response.body().getData().getCreatedByName());
                            tvTglNews.setText(response.body().getData().getCreatedAt());
                            tvContent.setText(response.body().getData().getContent());
                            tvDiffDate.setText(response.body().getData().getDiffCreatedAt());
                            fileUrlShare = response.body().getData().getLinkShare();
                            urlImage = response.body().getData().getNewsImageUrl();
                            titleNews = response.body().getData().getTitle();


                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(DetailNewsActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.body().getCode().equals("05")){
                        String desc = response.body().getDesc();
                        Toast.makeText(DetailNewsActivity.this, desc, Toast.LENGTH_SHORT).show();
                        gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
                        sess.clearSess();
                        Intent i = new Intent(DetailNewsActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }  else {
                        String desc = response.body().getDesc();
                        Toast.makeText(DetailNewsActivity.this, desc, Toast.LENGTH_SHORT).show();
                        gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
                    }
                    gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
                }

                @Override
                public void onFailure(Call<GetInfoNewsResponseModel> call, Throwable t) {
                    Toast.makeText(DetailNewsActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    gm.setShimmerLinearLayout(false,shimmerLoad,llContent);

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}