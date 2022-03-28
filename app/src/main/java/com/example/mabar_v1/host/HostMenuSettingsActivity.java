package com.example.mabar_v1.host;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mabar_v1.R;
import com.example.mabar_v1.profile.CreateTournamentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HostMenuSettingsActivity extends AppCompatActivity {

    @BindView(R.id.btn_create_tournament)
    TextView btnCreateTournament;
    @BindView(R.id.btn_manage_tournament)
    TextView btnManageTournament;

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
                Intent i = new Intent(HostMenuSettingsActivity.this, HostManageTournamentActivity.class);
                startActivity(i);
            }
        });
    }
}