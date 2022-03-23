package com.example.mabar_v1.main;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asura.library.views.PosterSlider;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.mabar_v1.R;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.profile.DetailProfileAccountActivity;
import com.example.mabar_v1.profile.HostSettingNewActivity;
import com.example.mabar_v1.profile.TeamSettingActivity;
import com.example.mabar_v1.retrofit.RetrofitConfig;
import com.example.mabar_v1.retrofit.model.PersonnelResponseModel;
import com.example.mabar_v1.retrofit.model.SuccessResponseDefaultModel;
import com.example.mabar_v1.signup.SignUpActivity;
import com.example.mabar_v1.signup.model.ResponseRegisterModel;
import com.example.mabar_v1.utility.GlobalMethod;
import com.example.mabar_v1.utility.SessionUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileNewFragment extends Fragment {

    @BindView(R.id.btn_switch_account)
    TextView btnSwitchAccount;
    @BindView(R.id.iv_is_verif)
    ImageView ivIsVerified;
    @BindView(R.id.civ_profile)
    CircularImageView ivProfile;
    @BindView(R.id.id_akun)
    TextView tvIdAkun;
    @BindView(R.id.nama_akun)
    TextView tvNamaAkun;
    @BindView(R.id.btn_account)
    TextView btnAccount;
    @BindView(R.id.btn_team_settings)
    TextView btnTeamSettings;
    @BindView(R.id.btn_host_settings)
    TextView btnHostSettings;

    @BindView(R.id.btn_logout)
    TextView btnLogout;

    private SessionUser sess;
    private GlobalMethod gm;
    private Integer role = 1;
    private Dialog dialogSwitchAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sess = new SessionUser(getActivity());
        gm = new GlobalMethod();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_new, container, false);
        ButterKnife.bind(this, rootView);


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                gm.showDialogConfirmation(getActivity(), "Log Out", "Log out now?", "Log Out", "Cancel",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sess.clearSess();
                                Intent i = new Intent(getActivity(), LoginActivity.class);
                                startActivity(i);
                                getActivity().finish();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                gm.dismissDialogConfirmation();
                            }
                        });
            }
        });
        btnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DetailProfileAccountActivity.class);
                startActivity(i);
            }
        });
        btnTeamSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), TeamSettingActivity.class);
                startActivity(i);
            }
        });
        btnHostSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), HostSettingNewActivity.class);
                startActivity(i);
            }
        });


        btnSwitchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSwitchAccount();
            }
        });

        getDataPerson();

        return rootView;

    }

    private void requestTeamLeader(){
        ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Request Team Leader...");
        progress.show();
        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiServices(sess.getString("token")).personnelReqTeamLead(sess.getString("id_user"));
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                            dialogSwitchAccount.dismiss();
                            getActivity().recreate();
                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Failed Request Team Leader", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void requestHostTournament(){
        ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Request Host Tournament...");
        progress.show();
        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiServices(sess.getString("token")).personnelReqHost(sess.getString("id_user"));
            req.enqueue(new Callback<SuccessResponseDefaultModel>() {
                @Override
                public void onResponse(Call<SuccessResponseDefaultModel> call, Response<SuccessResponseDefaultModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                            dialogSwitchAccount.dismiss();
                            getActivity().recreate();


                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Failed Request Host Tournament", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getDataPerson(){
        ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setMessage("Getting Profile Info...");
        progress.show();
        try {
            Call<PersonnelResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getPersonnel(sess.getString("id_user"));
            req.enqueue(new Callback<PersonnelResponseModel>() {
                @Override
                public void onResponse(Call<PersonnelResponseModel> call, Response<PersonnelResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            if (response.body().getData().getImage() != null){
                                Glide.with(getActivity())
                                        .load(response.body().getData().getImage())
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(ivProfile);
                            }
                            if (response.body().getData().isVerified() == 1){
                                ivIsVerified.setVisibility(View.VISIBLE);
                            }else {
                                ivIsVerified.setVisibility(View.GONE);
                            }
                            tvNamaAkun.setText(sess.getString("username"));
                            tvIdAkun.setText("Id: "+sess.getString("id_user"));
                            role = response.body().getData().getRole();
                            if (role == 2){
                                btnHostSettings.setVisibility(View.GONE);
                            }else if (role == 3){
                                btnTeamSettings.setVisibility(View.GONE);
                            }else {
                                btnHostSettings.setVisibility(View.GONE);
                                btnTeamSettings.setVisibility(View.GONE);
                            }
                            btnSwitchAccount.setText("Logged in as : "+ gm.getRoleById(role));

                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            sess.clearSess();
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Failed Request Profil Info", Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<PersonnelResponseModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void showDialogSwitchAccount(){

        dialogSwitchAccount = new Dialog(getActivity());
        View dv  = getActivity().getLayoutInflater().inflate(R.layout.dialog_switch_account,null);
        dialogSwitchAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSwitchAccount.setContentView(dv);


        Button btnHost = (Button) dialogSwitchAccount.findViewById(R.id.btn_host);
        Button btnTeamLead = (Button) dialogSwitchAccount.findViewById(R.id.btn_team_lead);
        if (role == 2){
            btnHost.setBackground(getResources().getDrawable(R.drawable.rounded_corners_button_primary));
            btnHost.setEnabled(true);
            btnTeamLead.setBackground(getResources().getDrawable(R.drawable.rounded_button_grey));
            btnTeamLead.setEnabled(false);
        }else if (role == 3){
            btnHost.setBackground(getResources().getDrawable(R.drawable.rounded_button_grey));
            btnHost.setEnabled(false);
            btnTeamLead.setBackground(getResources().getDrawable(R.drawable.rounded_corners_button_primary));
            btnTeamLead.setEnabled(true);
        }else {
            btnHost.setBackground(getResources().getDrawable(R.drawable.rounded_corners_button_primary));
            btnHost.setEnabled(true);
            btnTeamLead.setBackground(getResources().getDrawable(R.drawable.rounded_corners_button_primary));
            btnTeamLead.setEnabled(true);
        }
        btnHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestHostTournament();
            }
        });
        btnTeamLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestTeamLeader();
            }
        });

        dialogSwitchAccount.show();

    }

}