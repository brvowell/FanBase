package com.example.brandonvowell.fanbase;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.google.firebase.auth.FirebaseAuth;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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

    private void setupMenuScreenListeners() {
        mAuth = FirebaseAuth.getInstance();

        final Button btnLogOut = (Button) findViewById(R.id.btnLogOut);
        //final Button btnHistory = (Button) findViewById(R.id.btnHistory);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //logout of firebase instance, go back to login activity
                mAuth.signOut();
                transitionLoginScreenActivity();
            }
        });

        /*btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transitionHistoryActivity();
            }
        });*/
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}