package com.circle.circle_games.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.circle.circle_games.login.model.ResponseLoginModel;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.signup.SignUpActivity;
import com.circle.circle_games.utility.SessionUser;
import com.circle.circle_games.R;
import com.circle.circle_games.login.model.ResponseLoginModel;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.signup.SignUpActivity;
import com.circle.circle_games.splash.SplashScreen1;
import com.circle.circle_games.utility.SessionUser;

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
    private String iUser,iEmail,iPassword = "";
    private SessionUser sess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        sess = new SessionUser(this);
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
                    getLoginData(iUser,iEmail,iPassword);
                }else {
                    Toast.makeText(LoginActivity.this, "Tolong Lengkapi Data Terlebih Dahulu", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getLoginData(String username,String email,String password){
        ProgressDialog progress = new ProgressDialog(LoginActivity.this);
        progress.setMessage("Loading...");
        progress.show();
        try {
            Call<ResponseLoginModel> req = RetrofitConfig.getApiServices("").login(username, email, password);
            req.enqueue(new Callback<ResponseLoginModel>() {
                @Override
                public void onResponse(Call<ResponseLoginModel> call, Response<ResponseLoginModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            sess.setString("token",response.body().getData().getAccessToken());
                            sess.setString("username",response.body().getData().getUser().getUsername());
                            sess.setString("email",response.body().getData().getUser().getEmail());
                            sess.setString("id_user",response.body().getData().getUser().getId()+"");
                            sess.commitSess();
                            String desc = response.body().getDesc();
                            Toast.makeText(LoginActivity.this, desc, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, SplashScreen1.class);
                            startActivity(i);
                            finish();
                        }else {
                            String notif = response.body().getDesc();
                            Toast.makeText(LoginActivity.this, notif, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Gagal Login", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseLoginModel> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Gagal Mengambil Data", Toast.LENGTH_SHORT).show();
                    System.out.println("onFailure"+call);
                    progress.dismiss();

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}