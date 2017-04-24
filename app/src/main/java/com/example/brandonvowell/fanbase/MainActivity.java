package com.example.brandonvowell.fanbase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar my_toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        //setSupportActionBar(my_toolbar);

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

        //final Button btnLogout = (Button) findViewById(R.id.btnLogout);
        final Button btnCreate = (Button) findViewById(R.id.btnCreate);
        final Button btnBrowse = (Button) findViewById(R.id.btnBrowse);
        final Button btnSettings = (Button) findViewById(R.id.btnSettings);
        //final Button btnHistory = (Button) findViewById(R.id.btnHistory);

        /*btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logout of firebase instance, go back to login activity
                mAuth.signOut();
                transitionLoginScreenActivity();
            }
        });*/

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

        /*btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionHistoryActivity();
            }
        });*/
    }
}