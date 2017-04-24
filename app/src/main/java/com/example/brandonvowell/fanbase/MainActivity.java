package com.example.brandonvowell.fanbase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            setupMenuScreenListeners();
        } else {
            transitionLoginScreenActivity();
        }
    }

    private void transitionLoginScreenActivity() {
        Intent nextScreen = new Intent(this, LoginScreenActivity.class);
        startActivityForResult(nextScreen, 0);
    }

    private void transitionTailgateInfoActivity() {
        Intent nextScreen = new Intent(this, TailgateInfoActivity.class);
        startActivityForResult(nextScreen, 0);
    }

    private void transitionTailgateMapActivity() {
        Intent nextScreen = new Intent(this, TailgateMapActivity.class);
        startActivityForResult(nextScreen, 0);
    }

    private void transitionSettingsActivity() {
        Intent nextScreen = new Intent(this, SettingsActivity.class);
        startActivityForResult(nextScreen, 0);
    }

    private void transitionHistoryActivity() {
        Intent nextScreen = new Intent(this, TailgateHistoryActivity.class);
        startActivityForResult(nextScreen, 0);
    }

    private void setupMenuScreenListeners() {
        mAuth = FirebaseAuth.getInstance();

        final ImageButton btnCreate = (ImageButton) findViewById(R.id.btnCreate);
        final ImageButton btnBrowse = (ImageButton) findViewById(R.id.btnBrowse);
        final ImageButton btnSettings = (ImageButton) findViewById(R.id.btnSettings);
        final ImageButton btnPrevTailgate = (ImageButton) findViewById(R.id.btnPrevTailgate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionTailgateInfoActivity();
            }
        });

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionTailgateMapActivity();
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionSettingsActivity();
            }
        });

        btnPrevTailgate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionHistoryActivity();
            }
        });
    }
}