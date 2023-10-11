package com.circle.circle_games.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.circle.circle_games.host.HostManageTournamentActivity;
import com.circle.circle_games.login.model.LoginResponseModel;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.signup.SignUpActivity;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.circle.circle_games.R;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.signup.SignUpActivity;
import com.circle.circle_games.splash.SplashScreen1;
import com.circle.circle_games.utility.SessionUser;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tvLupaPassword)
    TextView tvLupaPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.tvSignup)
    TextView tvSignUp;
    private String iUser,iEmail,iPassword, tokenFirebase = "";
    private SessionUser sess;
    private GlobalMethod gm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sess = new SessionUser(this);
        gm = new GlobalMethod();
        sess.clearSess();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iUser = etEmail.getText().toString();
                iEmail = etEmail.getText().toString();
                iPassword = etPassword.getText().toString();

                if (!iUser.trim().equals("")|| !iEmail.trim().equals("") || !iPassword.trim().equals("")){
                    getTokenFCM();
                }else {
                    Toast.makeText(LoginActivity.this, "Tolong Lengkapi Data Terlebih Dahulu", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getLoginData(String username,String email,String password, String tokenFirebase){
        gm.showLoadingDialog(LoginActivity.this);
        try {
            Call<LoginResponseModel> req = RetrofitConfig.getApiServices("").login(username, email, password, tokenFirebase);
            req.enqueue(new Callback<LoginResponseModel>() {
                @Override
                public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            if (!tokenFirebase.equals("")){
                                sess.setString("token",response.body().getData().getAccessToken());
                                sess.setString("username",response.body().getData().getUser().getUsername());
                                sess.setString("email",response.body().getData().getUser().getEmail());
                                sess.setString("id_user",response.body().getData().getUser().getId()+"");
                                sess.setString("id_team",response.body().getData().getDetailPersonnel().getTeamId());
                                sess.commitSess();
                                String desc = response.body().getDesc();
                                Toast.makeText(LoginActivity.this, desc, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(LoginActivity.this, SplashScreen1.class);
                                startActivity(i);
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this,"Failed to get Device Token",Toast.LENGTH_SHORT);
                            }


                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(LoginActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Gagal Login", Toast.LENGTH_SHORT).show();
                    }
                    gm.dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    gm.dismissLoadingDialog();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getTokenFCM(){
        gm.showLoadingDialog(LoginActivity.this);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            if (!TextUtils.isEmpty(token)) {
                Log.d("TOKEN FCM", "retrieve token successful : " + token);
                tokenFirebase = token;
                gm.dismissLoadingDialog();
                getLoginData(iUser,iEmail,iPassword,tokenFirebase);
            } else{
                Log.w("TOKEN FCM", "token should not be null...");
                gm.dismissLoadingDialog();
                Toast.makeText(this,"Failed to get Device Token",Toast.LENGTH_SHORT);
            }
        }).addOnFailureListener(e -> {
            gm.dismissLoadingDialog();
            //handle e
        }).addOnCanceledListener(() -> {
            gm.dismissLoadingDialog();
            //handle cancel
        }).addOnCompleteListener(task ->
                Log.v("TOKEN FCM", "This is the token : " + task.getResult()));
        gm.dismissLoadingDialog();

    }

}