package com.circle.circle_games;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.main.adapter.ListNewsHomeAdapter;
import com.circle.circle_games.main.adapter.ListPaymentAdapter;
import com.circle.circle_games.main.adapter.ListTeamAdapter;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.GetListTeamResponseModel;
import com.circle.circle_games.retrofit.model.ListPaymentResponseModel;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {

    @BindView(R.id.rv_payment)
    RecyclerView rvPayment;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.shimmer_load)
    ShimmerFrameLayout shimmerLoad;
    @BindView(R.id.btn_back)
    ImageView btnBack;

    private GlobalMethod gm;
    private SessionUser sess;

    private ListPaymentAdapter paymentAdapter;
    private ListPaymentResponseModel.Data listPaymentModels;
    List<ListPaymentResponseModel.Data> listPayment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        gm = new GlobalMethod();
        sess = new SessionUser(this);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            JSONArray an = new JSONArray("[\n" +
                    "        {\n" +
                    "            \"id\": 101,\n" +
                    "            \"id_user\": 8,\n" +
                    "            \"payment\": \"Link Aja\",\n" +
                    "            \"id_payment\": \"halo2\",\n" +
                    "            \"created_at\": \"2022-02-27T07:40:09.000000Z\",\n" +
                    "            \"updated_at\": \"2022-06-26T11:46:05.000000Z\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": 102,\n" +
                    "            \"id_user\": 8,\n" +
                    "            \"payment\": \"Gopay\",\n" +
                    "            \"id_payment\": \"anu12\",\n" +
                    "            \"created_at\": \"2022-02-27T07:40:09.000000Z\",\n" +
                    "            \"updated_at\": \"2022-06-26T11:46:05.000000Z\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": 103,\n" +
                    "            \"id_user\": 8,\n" +
                    "            \"payment\": \"Ovo\",\n" +
                    "            \"id_payment\": \"holaaa\",\n" +
                    "            \"created_at\": \"2022-02-27T07:40:09.000000Z\",\n" +
                    "            \"updated_at\": \"2022-06-26T11:46:05.000000Z\"\n" +
                    "        }\n" +
                    "    ]");

            for (int i = 0; i<an.length();i++){
                listPaymentModels = new ListPaymentResponseModel.Data(
                        an.getJSONObject(i).getString("created_at"),
                        an.getJSONObject(i).getInt("id"),
                        an.getJSONObject(i).getString("id_payment"),
                        an.getJSONObject(i).getInt("id_user"),
                        an.getJSONObject(i).getString("payment"),
                        an.getJSONObject(i).getString("updated_at")
                        );
                listPayment.add(listPaymentModels);
            }
            paymentAdapter = new ListPaymentAdapter(PaymentActivity.this,listPayment);
            rvPayment.setAdapter(paymentAdapter);
            rvPayment.setLayoutManager(new LinearLayoutManager(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}