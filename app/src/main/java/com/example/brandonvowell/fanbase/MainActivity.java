package com.example.brandonvowell.fanbase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO
        //If user is authenticated, start menu activity.
        //If user is not authenticated, start login activity.

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            mAuth = FirebaseAuth.getInstance();

            final Button btnLogout = (Button) findViewById(R.id.btnLogout);

            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //logout of firebase instance, go back to login activity
                    mAuth.signOut();
                    transitionLoginScreenActivity();
                }
            });
        } else {
            Intent nextScreen = new Intent(this, LoginScreenActivity.class);
            startActivityForResult(nextScreen, 0);
        }
    }

    private void transitionLoginScreenActivity() {
        Intent nextScreen = new Intent(this, LoginScreenActivity.class);
        startActivityForResult(nextScreen, 0);
    }
}
