package com.example.brandonvowell.fanbase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO
        //If user is authenticated, start menu activity.
        //If user is not authenticated, start login activity.
        setContentView(R.layout.activity_login);

        final Button btnRegister = (Button) findViewById(R.id.btnRegister);
        final Button btnLogin = (Button) findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(v.getContext(), RegisterScreen.class);
                startActivityForResult(nextScreen, 0);
            }
        });
    }
}
