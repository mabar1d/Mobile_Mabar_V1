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
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.SdkUIFlowBuilder;
import com.midtrans.sdk.uikit.external.UiKitApi;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements TransactionFinishedCallback {

    @BindView(R.id.btn_back)
    ImageView btnBack;
    @BindView(R.id.btn_pay)
    Button btnPay;
    @BindView(R.id.tv_total)
    TextView tvTotal;

    private GlobalMethod gm;
    private SessionUser sess;
    private String idTournament = "";
    private String idUser = "";
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

        idUser = sess.getString("id_user");

        tvTotal.setText("Total : "+ fee);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        makePayment();

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPay();

            }
        });

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

    private void makePayment(){
        SdkUIFlowBuilder.init()
                .setClientKey(BuildConfig.CLIENT_KEY_MIDTRANS)
                .setContext(getApplicationContext())
                .setTransactionFinishedCallback(this)
                .setMerchantBaseUrl(/*"url yang index.php pake /"*/BuildConfig.BASE_URL_MIDTRANS)
                .enableLog(true)
                .setColorTheme(new CustomColorTheme("#777777","#f77474" , "#3f0d0d"))
                .setLanguage("id")
                .buildSDK();

        /*SdkUIFlowBuilder.init()
                .setContext(this)
                .setMerchantBaseUrl(BuildConfig.BASE_URL)
                .setClientKey(BuildConfig.CLIENT_KEY)
                .setTransactionFinishedCallback(this)
                .enableLog(true)
                .setColorTheme(new CustomColorTheme("#777777","#f77474" , "#3f0d0d"))
                .buildSDK();*/
    }
    private void clickPay(){
        MidtransSDK.getInstance().setTransactionRequest(transactionRequest("Tournament-"+idTournament, Double.parseDouble(fee), 1, "Nama Turnament"));
        MidtransSDK.getInstance().startPaymentUiFlow(PaymentActivity.this );
    }
    public TransactionRequest transactionRequest(String id, Double price, int qty, String name){
        TransactionRequest request =  new TransactionRequest("CG-"+ idUser +"-"+System.currentTimeMillis(), price );
        request.setCustomerDetails(customerDetails());
        ItemDetails details = new ItemDetails(id, price, qty, name);

        ArrayList<ItemDetails> itemDetails = new ArrayList<>();
        itemDetails.add(details);
        request.setItemDetails(itemDetails);

        return request;
    }

    public static CustomerDetails customerDetails(){
        CustomerDetails cd = new CustomerDetails();
        cd.setFirstName("NAMAMU");
        cd.setLastName("Bagus");
        cd.setEmail("email@gmail.com");
        cd.setPhone("0897354772537");
        return cd;
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {

        if(result.getResponse() != null){
            switch (result.getStatus()){
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(this, "Transaction Sukses " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this, "Transaction Pending " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
            }
            result.getResponse().getStatusMessage();
        }else if(result.isTransactionCanceled()){
            Toast.makeText(this, "Transaction Failed", Toast.LENGTH_LONG).show();
        }else{
            if(result.getStatus().equalsIgnoreCase((TransactionResult.STATUS_INVALID))){
                Toast.makeText(this, "Transaction Invalid" + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Something Wrong", Toast.LENGTH_LONG).show();
            }
        }
    }
}