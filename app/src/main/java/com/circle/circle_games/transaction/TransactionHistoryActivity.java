package com.circle.circle_games.transaction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.circle.circle_games.R;
import com.circle.circle_games.transaction.fragment.TransactionFailedFragment;
import com.circle.circle_games.transaction.fragment.TransactionPendingFragment;
import com.circle.circle_games.transaction.fragment.TransactionSuccessFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionHistoryActivity extends AppCompatActivity {

    @BindView(R.id.btn_back)
    ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Initialize the three fragments
        TransactionSuccessFragment fragmentSuccess = new TransactionSuccessFragment();
        TransactionPendingFragment fragmentPending = new TransactionPendingFragment();
        TransactionFailedFragment fragmentFailed = new TransactionFailedFragment();

        // Load the first fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragmentSuccess).commit();

        // Set up the bottom navigation view
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                // Switch between fragments based on the selected menu item
                switch (item.getItemId()) {
                    case R.id.nav_fragment_success:
                        selectedFragment = fragmentSuccess;
                        break;
                    case R.id.nav_fragment_pending:
                        selectedFragment = fragmentPending;
                        break;
                    case R.id.nav_fragment_failed:
                        selectedFragment = fragmentFailed;
                        break;
                }

                // Load the selected fragment
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment).commit();

                return true;
            }
        });
    }
}