package com.circle.circle_games.main;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.circle.circle_games.host.HostMenuSettingsActivity;
import com.circle.circle_games.profile.DetailProfileAccountActivity;
import com.circle.circle_games.profile.JoinTeamActivity;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.PersonnelResponseModel;
import com.circle.circle_games.retrofit.model.SuccessResponseDefaultModel;
import com.circle.circle_games.team.DetailTeamInfoActivity;
import com.circle.circle_games.team.TeamSettingsActivity;
import com.circle.circle_games.transaction.TransactionHistoryActivity;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.circle.circle_games.R;
import com.circle.circle_games.host.HostMenuSettingsActivity;
import com.circle.circle_games.login.LoginActivity;
import com.circle.circle_games.profile.DetailProfileAccountActivity;
import com.circle.circle_games.profile.JoinTeamActivity;
import com.circle.circle_games.retrofit.RetrofitConfig;
import com.circle.circle_games.retrofit.model.PersonnelResponseModel;
import com.circle.circle_games.retrofit.model.SuccessResponseDefaultModel;
import com.circle.circle_games.team.DetailTeamInfoActivity;
import com.circle.circle_games.team.TeamSettingsActivity;
import com.circle.circle_games.utility.GlobalMethod;
import com.circle.circle_games.utility.SessionUser;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.mikhaellopez.circularimageview.CircularImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileNewFragment extends Fragment {


    @BindView(R.id.iv_is_verif)
    ImageView ivIsVerified;
    @BindView(R.id.civ_profile)
    CircularImageView ivProfile;
    @BindView(R.id.id_akun)
    TextView tvIdAkun;
    @BindView(R.id.nama_akun)
    TextView tvNamaAkun;

    //Btn
    @BindView(R.id.btn_account)
    RelativeLayout btnAccount;
    @BindView(R.id.btn_team_settings)
    RelativeLayout btnTeamSettings;
    @BindView(R.id.btn_host_settings)
    RelativeLayout btnHostSettings;
    @BindView(R.id.btn_join_team)
    RelativeLayout btnJoinTeam;
    @BindView(R.id.btn_my_team)
    RelativeLayout btnMyTeam;
    @BindView(R.id.btn_transaction_history)
    RelativeLayout btnTransactionHistory;

    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.shimmer_load)
    ShimmerFrameLayout shimmerLoad;

    @BindView(R.id.btn_logout)
    RelativeLayout btnLogout;
    @BindView(R.id.btn_faq)
    RelativeLayout btnFaq;
    @BindView(R.id.btn_switch_account)
    RelativeLayout btnSwitchAccount;
    @BindView(R.id.tv_switch_account)
    TextView tvSwitchAccount;

    private SessionUser sess;
    private GlobalMethod gm;
    private Integer role = 1;
    private Dialog dialogSwitchAccount;
    private String idTeam = "";
    public Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sess = new SessionUser(getActivity());
        gm = new GlobalMethod();
        context = getContext();

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
                Intent i = new Intent(getActivity(), TeamSettingsActivity.class);
                startActivity(i);
            }
        });
        btnHostSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), HostMenuSettingsActivity.class);
                startActivity(i);
            }
        });
        btnJoinTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), JoinTeamActivity.class);
                startActivity(i);
            }
        });

        btnMyTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), DetailTeamInfoActivity.class);
                Bundle bun = new Bundle();
                bun.putString("id_team", idTeam );
                bun.putString("flag_team", "Y");
                i.putExtras(bun);
                startActivity(i);
            }
        });

        btnTransactionHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), TransactionHistoryActivity.class);
                startActivity(i);
            }
        });

        btnSwitchAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSwitchAccount();
            }
        });

        //getDataPerson();

        return rootView;

    }

    private void requestTeamLeader(){
        gm.showLoadingDialog(getActivity());
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
                            gm.dismissLoadingDialog();
                            sess.clearSess();
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Failed Log in as Team Leader", Toast.LENGTH_SHORT).show();
                        gm.dismissLoadingDialog();
                    }
                    gm.dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    gm.dismissLoadingDialog();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void requestHostTournament(){
        gm.showLoadingDialog(getActivity());
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
                            gm.dismissLoadingDialog();
                            sess.clearSess();
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Failed Log in as Host Tournament", Toast.LENGTH_SHORT).show();
                        gm.dismissLoadingDialog();
                    }
                    gm.dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    gm.dismissLoadingDialog();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void requestMember(){
        gm.showLoadingDialog(getActivity());
        try {
            Call<SuccessResponseDefaultModel> req = RetrofitConfig.getApiServices(sess.getString("token")).personnelReqMember(sess.getString("id_user"));
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
                            gm.dismissLoadingDialog();
                            sess.clearSess();
                            Intent i = new Intent(getActivity(), LoginActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }else {
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(getActivity(), "Failed Log in as Member", Toast.LENGTH_SHORT).show();
                        gm.dismissLoadingDialog();
                    }
                    gm.dismissLoadingDialog();
                }

                @Override
                public void onFailure(Call<SuccessResponseDefaultModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    gm.dismissLoadingDialog();
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void getDataPerson(){
        gm.setShimmerLinearLayout(true,shimmerLoad,llContent);
        try {
            Call<PersonnelResponseModel> req = RetrofitConfig.getApiServices(sess.getString("token")).getPersonnel(sess.getString("id_user"));
            req.enqueue(new Callback<PersonnelResponseModel>() {
                @Override
                public void onResponse(Call<PersonnelResponseModel> call, Response<PersonnelResponseModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getCode().equals("00")){

                            if (response.body().getData().getImage() != null){
                                CircularProgressDrawable cp = new CircularProgressDrawable(context);
                                cp.setStrokeWidth(5f);
                                //cp.setBackgroundColor(R.color.material_grey_300);
                                cp.setColorSchemeColors(R.color.primary_color_black, R.color.material_grey_800, R.color.material_grey_900);
                                cp.setCenterRadius(30f);
                                cp.start();

                                Glide.with(context)
                                        .load(response.body().getData().getImage())
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .placeholder(cp)
                                        .error(R.drawable.not_found)
                                        .skipMemoryCache(true)
                                        .into(ivProfile);
                            }
                            idTeam = response.body().getData().getTeamId();
                            if (response.body().getData().isVerified() == 1){
                                ivIsVerified.setVisibility(View.VISIBLE);
                            }else {
                                ivIsVerified.setVisibility(View.GONE);
                            }
                            tvNamaAkun.setText(sess.getString("username"));
                            tvIdAkun.setText("Id: "+sess.getString("id_user"));
                            role = response.body().getData().getRole();
                            btnAccount.setVisibility(View.VISIBLE);
                            if (role == 2){
                                btnHostSettings.setVisibility(View.GONE);
                                btnJoinTeam.setVisibility(View.GONE);
                                btnMyTeam.setVisibility(View.GONE);
                                btnTeamSettings.setVisibility(View.VISIBLE);
                                btnTransactionHistory.setVisibility(View.VISIBLE);
                            }else if (role == 3){
                                btnTeamSettings.setVisibility(View.GONE);
                                btnTransactionHistory.setVisibility(View.GONE);
                                btnHostSettings.setVisibility(View.VISIBLE);
                                btnJoinTeam.setVisibility(View.GONE);
                            }else {
                                btnHostSettings.setVisibility(View.GONE);
                                btnTeamSettings.setVisibility(View.GONE);
                                btnTransactionHistory.setVisibility(View.GONE);
                                if (idTeam != null){
                                    btnMyTeam.setVisibility(View.VISIBLE);
                                    btnJoinTeam.setVisibility(View.GONE);
                                }else {
                                    btnMyTeam.setVisibility(View.GONE);
                                    btnJoinTeam.setVisibility(View.VISIBLE);
                                }
                            }
                            tvSwitchAccount.setText("Logged in as : "+ gm.getRoleById(role));


                        }else if (response.body().getCode().equals("05")){
                            String desc = response.body().getDesc();
                            Toast.makeText(getActivity(), desc, Toast.LENGTH_SHORT).show();
                            gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
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
                        gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
                    }
                    gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
                }

                @Override
                public void onFailure(Call<PersonnelResponseModel> call, Throwable t) {
                    String msg = t.getMessage();
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    gm.setShimmerLinearLayout(false,shimmerLoad,llContent);
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //1 member, 2 Captain, 3 Host
    public void showDialogSwitchAccount(){

        dialogSwitchAccount = new Dialog(getActivity());
        View dv  = getActivity().getLayoutInflater().inflate(R.layout.dialog_switch_account,null);
        dialogSwitchAccount.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogSwitchAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSwitchAccount.setContentView(dv);


        Button btnSatu = (Button) dialogSwitchAccount.findViewById(R.id.btn_satu);
        Button btnDua = (Button) dialogSwitchAccount.findViewById(R.id.btn_dua);
        if (role == 2){
            btnSatu.setText("Host");
            btnDua.setText("Member");
            btnSatu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestHostTournament();
                }
            });
            btnDua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestMember();
                }
            });

        }else if (role == 3){
            btnSatu.setText("Captain");
            btnDua.setText("Member");
            btnSatu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestTeamLeader();
                }
            });
            btnDua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestMember();
                }
            });

        }else {
            btnSatu.setText("Host");
            btnDua.setText("Captain");
            btnSatu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestHostTournament();
                }
            });
            btnDua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestTeamLeader();
                }
            });

        }


        dialogSwitchAccount.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        getDataPerson();
    }
}