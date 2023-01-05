package com.circle.circle_games;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
import com.circle.circle_games.main.DetailTournamentActivity;
import com.circle.circle_games.main.adapter.ListNewsHomeAdapter;
import com.circle.circle_games.main.adapter.ListPaymentAdapter;
import com.circle.circle_games.main.adapter.ListTeamAdapter;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.GetListTeamResponseModel;
import com.circle.circle_games.retrofit.model.ListPaymentResponseModel;
import com.circle.circle_games.retrofit.model.SuccessResponseDefaultModel;
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
    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_pay)
    Button btnPay;
    @BindView(R.id.tv_total)
    TextView tvTotal;

    private GlobalMethod gm;
    private SessionUser sess;
    private String idTournament = "";
    private String fee = "";
    private String paymentMethod = "";

    private ListPaymentAdapter paymentAdapter;
    private ListPaymentResponseModel.Data listPaymentModels;
    List<ListPaymentResponseModel.Data> listPayment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);

        Bundle b = getIntent().getExtras();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if(b != null) {
            idTournament = b.getString("id_tournament");
            fee = b.getString("fee");
        }

        gm = new GlobalMethod();
        sess = new SessionUser(this);

        tvTotal.setText("Total : "+ fee);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!paymentMethod.equalsIgnoreCase("")){
                    gm.showDialogConfirmation(PaymentActivity.this, "Using "+ paymentMethod+"?", fee,
                            "Pay", "Cancel", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.e("Hasil",paymentMethod);
                                    //registerTournament(sess.getString("id_user"),idTournament);
                                    gm.dismissDialogConfirmation();

                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    gm.dismissDialogConfirmation();
                                }
                            });
                }else {
                    Toast.makeText(PaymentActivity.this,"Choose Payment Method..",Toast.LENGTH_SHORT).show();
                }

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
                    "        },{\n" +
                    "            \"id\": 104,\n" +
                    "            \"id_user\": 8,\n" +
                    "            \"payment\": \"Dana\",\n" +
                    "            \"id_payment\": \"-\",\n" +
                    "            \"created_at\": \"2022-02-27T07:40:09.000000Z\",\n" +
                    "            \"updated_at\": \"2022-06-26T11:46:05.000000Z\"\n" +
                    "        },{\n" +
                    "            \"id\": 105,\n" +
                    "            \"id_user\": 8,\n" +
                    "            \"payment\": \"Coin Circle\",\n" +
                    "            \"id_payment\": \"Rp.30.200.000\",\n" +
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
            paymentAdapter = new ListPaymentAdapter(PaymentActivity.this, listPayment, new ListPaymentAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ListPaymentResponseModel.Data item, int position) {
                    paymentMethod = item.getPayment();
                }
            });
            rvPayment.setAdapter(paymentAdapter);
            rvPayment.setLayoutManager(new LinearLayoutManager(this));

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void registerTournament(String userId,String idTournament){
        ProgressDialog progress = new ProgressDialog(this);
        progress.setMessage("Loading...");
        progress.show();
        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiServices("").registerTournament(userId, idTournament);
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            String notif = response.body().getDesc();
                            Toast.makeText(PaymentActivity.this, notif, Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(PaymentActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.body().getCode().equals("05")){
                        String desc = response.body().getDesc();
                        Toast.makeText(PaymentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                        sess.clearSess();
                        Intent i = new Intent(PaymentActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }  else {
                        String desc = response.body().getDesc();
                        Toast.makeText(PaymentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    Toast.makeText(PaymentActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    progress.dismiss();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}