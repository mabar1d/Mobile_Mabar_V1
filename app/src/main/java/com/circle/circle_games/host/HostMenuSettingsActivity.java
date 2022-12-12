package com.circle.circle_games.host;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.circle.circle_games.R;
import com.circle.circle_games.profile.CreateTournamentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HostMenuSettingsActivity extends AppCompatActivity {

    @BindView(R.id.btn_create_tournament)
    RelativeLayout btnCreateTournament;
    @BindView(R.id.btn_manage_tournament)
    RelativeLayout btnManageTournament;
    @BindView(R.id.btn_on_going_tournament)
    RelativeLayout btnOnGoingTournament;
    @BindView(R.id.btn_history_tournament)
    ImageView btnHistoryTournament;
    @BindView(R.id.tv_history)
    TextView tvHistory;
    @BindView(R.id.btn_back)
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_menu_settings);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnCreateTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HostMenuSettingsActivity.this, CreateTournamentActivity.class);
                startActivity(i);
            }
        });

        btnManageTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HostMenuSettingsActivity.this, HostListTournamentActivity.class);
                startActivity(i);
            }
        });

        btnOnGoingTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HostMenuSettingsActivity.this, HostListOnGoingTournamentActivity.class);
                startActivity(i);
            }
        });
        btnHistoryTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HostMenuSettingsActivity.this, HostListHistoryTournamentActivity.class);
                startActivity(i);
            }
        });
        tvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HostMenuSettingsActivity.this, HostListHistoryTournamentActivity.class);
                startActivity(i);
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}