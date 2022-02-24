package com.example.mabar_v1.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mabar_v1.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GeneralSearchTournamentActivity extends AppCompatActivity {

    @BindView(R.id.recycler_my_tournaments)
    RecyclerView rvTournament;
    @BindView(R.id.search_bar_tournament)
    EditText etSearchBarTournament;
    @BindView(R.id.btn_search)
    Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general_search_tournament);
        ButterKnife.bind(this);
    }
}