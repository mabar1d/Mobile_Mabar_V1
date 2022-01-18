package com.example.mabar_v1.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.asura.library.views.PosterSlider;
import com.example.mabar_v1.R;
import com.example.mabar_v1.login.LoginActivity;
import com.example.mabar_v1.utility.SessionUser;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileNewFragment extends Fragment {
    @BindView(R.id.btn_req_team_lead)
    Button btnReqTeamLead;
    @BindView(R.id.btn_req_host)
    Button btnReqHost;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sess = new SessionUser(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile_new, container, false);
        ButterKnife.bind(this, rootView);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sess.clearSess();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        return rootView;

    }
}