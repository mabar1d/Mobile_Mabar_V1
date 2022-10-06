package com.circle.circle_games.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.circle.circle_games.signup.model.ResponseRegisterModel;
import com.circle.circle_games.utility.SessionUser;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.R;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.signup.model.ResponseRegisterModel;
import com.circle.circle_games.utility.SessionUser;

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
    private SessionUser sess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        gm = new GlobalMethod();
        sess = new SessionUser(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
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
            Call<ResponseRegisterModel> req = RetrofitConfig.getApiServices(sess.getString("token")).register(username, email, password, verifPassword);
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
                            String desc = response.body().getDesc();
                            Toast.makeText(SignUpActivity.this, desc, Toast.LENGTH_SHORT).show();
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