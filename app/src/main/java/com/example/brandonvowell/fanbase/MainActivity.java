package com.example.brandonvowell.fanbase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO
        //If user is authenticated, start menu activity.
        //If user is not authenticated, start login activity.
        Intent nextScreen = new Intent(this, LoginScreenActivity.class);
        startActivityForResult(nextScreen, 0);
    }
}
