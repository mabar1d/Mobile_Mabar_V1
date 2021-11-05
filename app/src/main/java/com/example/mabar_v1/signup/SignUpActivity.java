package com.example.mabar_v1.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mabar_v1.GlobalMethod;
import com.example.mabar_v1.MainActivity;
import com.example.mabar_v1.R;
import com.example.mabar_v1.WelcomeActivity;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.retrofit.RetrofitConfig;
import com.example.mabar_v1.signup.model.ResponseRegisterModel;
import com.example.mabar_v1.splash.SplashScreenActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.etVerifPassword)
    EditText etVerifPassword;
    @BindView(R.id.btnSignup)
    Button btnSignup;
    @BindView(R.id.tvLogin)
    TextView tvLogin;

    String iUser = "";
    String iEmail = "";
    String iPassword = "";
    String iVerifPassword = "";

    GlobalMethod gm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        gm = new GlobalMethod();

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iUser = etUsername.getText().toString();
                iEmail = etEmail.getText().toString();
                iPassword = etPassword.getText().toString();
                iVerifPassword = etVerifPassword.getText().toString();

                if (!iUser.trim().equals("")|| !iEmail.trim().equals("") || !iPassword.trim().equals("") || !iVerifPassword.trim().equals("")){
                    sendRegisterData(iUser,iEmail,iPassword,iVerifPassword);
                }else {
                    Toast.makeText(SignUpActivity.this, "Tolong Lengkapi Data Terlebih Dahulu", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void sendRegisterData(String username,String email,String password,String verifPassword){
        ProgressDialog progress = new ProgressDialog(SignUpActivity.this);
        progress.setMessage("Loading...");
        progress.show();
        try {
            Call<ResponseRegisterModel> req = RetrofitConfig.getApiServices().register(username, email, password, verifPassword);
            req.enqueue(new Callback<ResponseRegisterModel>() {
                @Override
                public void onResponse(Call<ResponseRegisterModel> call, Response<ResponseRegisterModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            String desc = response.body().getDesc();
                            Toast.makeText(SignUpActivity.this, desc, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(i);
                        }else {

                        }

                    } else {
                        Toast.makeText(SignUpActivity.this, "Gagal Registrasi", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseRegisterModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}