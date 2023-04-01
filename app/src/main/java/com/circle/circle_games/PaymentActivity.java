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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.main.DetailTournamentActivity;
import com.circle.circle_games.main.ProfileNewFragment;
import com.circle.circle_games.main.adapter.ListNewsHomeAdapter;
import com.circle.circle_games.main.adapter.ListPaymentAdapter;
import com.circle.circle_games.main.adapter.ListTeamAdapter;
import com.circle.circle_games.profile.DetailProfileAccountActivity;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.GetListTeamResponseModel;
import com.circle.circle_games.retrofit.model.ListPaymentResponseModel;
import com.circle.circle_games.retrofit.model.PersonnelResponseModel;
import com.circle.circle_games.retrofit.model.SuccessResponseDefaultModel;
import com.circle.circle_games.team.TeamSettingsActivity;
import com.circle.circle_games.transaction.TransactionHistoryActivity;
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
    @BindView(R.id.tv_first_name)
    TextView tvFirstName;
    @BindView(R.id.tv_last_name)
    TextView tvLastName;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;

    private GlobalMethod gm;
    private SessionUser sess;
    private String idTournament = "";
    private String idUser = "";
    private String fee = "";
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String phone = "";

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

        getDataPerson();
        tvTotal.setText("Total : "+ fee);
        if (fee.contains(".")){
            fee = fee.replace(".","");
        }
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPay();

            }
        });

    }

    private void getDataPerson(){
        ProgressDialog progress = new ProgressDialog(PaymentActivity.this);
        progress.setMessage("Getting Profile Info...");
        progress.show();
        try {
            Call<PersonnelResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getPersonnel(sess.getString("id_user"));
            req.enqueue(new Callback<PersonnelResponseModel>() {
                @Override
                public void onResponse(Call<PersonnelResponseModel> call, Response<PersonnelResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            firstName = response.body().getData().getFirstname();
                            lastName = response.body().getData().getLastname();
                            email = response.body().getData().getEmail();
                            phone = response.body().getData().getPhone();

                            tvFirstName.setText(firstName);
                            tvLastName.setText(lastName);
                            tvEmail.setText(email);
                            tvPhoneNumber.setText(phone);

                            makePayment();


                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(PaymentActivity.this, desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(PaymentActivity.this, LoginActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(PaymentActivity.this, desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(PaymentActivity.this, "Failed Request Profil Info", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<PersonnelResponseModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(PaymentActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
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

    private void makePayment(){
        SdkUIFlowBuilder.init()
                .setClientKey(BuildConfig.CLIENT_KEY_MIDTRANS)
                .setContext(getApplicationContext())
                .setTransactionFinishedCallback(this)
                .setMerchantBaseUrl(BuildConfig.BASE_URL_MIDTRANS)
                .enableLog(true)
                .setColorTheme(new CustomColorTheme("#777777","#f77474" , "#3f0d0d"))
                .setLanguage("id")
                .buildSDK();
    }
    private void clickPay(){
        MidtransSDK.getInstance().setTransactionRequest(transactionRequest("Tournament-"+idTournament, Double.parseDouble(fee), 1, "Nama Turnament"));
        MidtransSDK.getInstance().startPaymentUiFlow(PaymentActivity.this );
        MidtransSDK.getInstance().startPaymentUiFlow(PaymentActivity.this);
    }
    public TransactionRequest transactionRequest(String id, Double price, int qty, String name){
        TransactionRequest request =  new TransactionRequest("CG-"+"TR-"+ idUser +"-"+System.currentTimeMillis()+"-"+idTournament, price );
        request.setCustomerDetails(customerDetails(firstName,lastName,email,phone));
        ItemDetails details = new ItemDetails(id, price, qty, name);

        ArrayList<ItemDetails> itemDetails = new ArrayList<>();
        itemDetails.add(details);
        request.setItemDetails(itemDetails);

        return request;
    }

    public static CustomerDetails customerDetails(String firstName,String lastName, String email, String phone){
        CustomerDetails cd = new CustomerDetails();
        cd.setFirstName(firstName);
        cd.setLastName(lastName);
        cd.setEmail(email);
        cd.setPhone(phone);
        return cd;
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {

        if(result.getResponse() != null){
            switch (result.getStatus()){
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(this, "Transaction Sukses " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    registerTournament(idUser, idTournament);
                    break;
                case TransactionResult.STATUS_PENDING:
                    gm.showDialogConfirmation(PaymentActivity.this, "Transaction on progress", "Please check your transaction regularly",
                            "Check Transaction", "Back", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(PaymentActivity.this, TransactionHistoryActivity.class);
                                    Bundle bun = new Bundle();
                                    bun.putString("id_tournament", idTournament);
                                    i.putExtras(bun);
                                    startActivity(i);
                                    gm.dismissDialogConfirmation();
                                    finish();

                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    gm.dismissDialogConfirmation();
                                    finish();
                                }
                            });
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