package com.example.mabar_v1.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mabar_v1.R;
import com.example.mabar_v1.WelcomeActivity;
import com.example.mabar_v1.login.model.ResponseLoginModel;
import com.example.mabar_v1.retrofit.RetrofitConfig;
import com.example.mabar_v1.signup.SignUpActivity;
import com.example.mabar_v1.signup.model.ResponseRegisterModel;
import com.example.mabar_v1.splash.SplashScreenActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
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
            Call<ResponseLoginModel> req = RetrofitConfig.getApiServices().login(username, email, password);
            req.enqueue(new Callback<ResponseLoginModel>() {
                @Override
                public void onResponse(Call<ResponseLoginModel> call, Response<ResponseLoginModel> response) {
                    if (response.isSuccessful()) {
                        String desc = response.body().getDesc();
                        Toast.makeText(LoginActivity.this, desc, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(LoginActivity.this, SplashScreenActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Gagal Login", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseLoginModel> call, Throwable t) {

                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}