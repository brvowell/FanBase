package com.example.brandonvowell.fanbase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    DatabaseReference database;
    private ArrayList<Tailgate> tailgateList;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            setupMenuScreenListeners();
        } else {
            transitionLoginScreenActivity();
        }

        database = FirebaseDatabase.getInstance().getReference().child("Tailgates");
        tailgateList = new ArrayList<Tailgate>();

        Button refreshButton = (Button) findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Get map of users in datasnapshot
                                collectTailgates((Map<String,Object>) dataSnapshot.getValue());
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //handle databaseError
                            }
                        });
            }
        });

        database.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Get map of users in datasnapshot
                        collectTailgates((Map<String,Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                });
        this.onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            String tailgateIdentifier = data.substring(data.lastIndexOf("/") + 1);
            //Here we need to download tailgate based on identifier then transition to tailgate detail activity
            System.out.println(tailgateIdentifier);
            deepLinkDetailTransition(tailgateIdentifier);
        }
    }

    private void deepLinkDetailTransition(String identifier) {
        for (Tailgate tailgate : tailgateList) {
            if (tailgate.tailgateIdentifier == identifier) {
                Intent nextScreen = new Intent(this, TailgateDetailActivity.class);
                nextScreen.putExtra("TAILGATE_OBJECT", tailgate);
                startActivityForResult(nextScreen, 0);
            }
        }
        //If made it here, there is no tailgate with that identifier
        Toast.makeText(MainActivity.this, "The link you clicked was invalid.", Toast.LENGTH_SHORT).show();
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

    private void collectTailgates(Map<String,Object> tailgates) {
        ArrayList<Tailgate> tL = new ArrayList<Tailgate>();
        for (Map.Entry<String, Object> entry : tailgates.entrySet()){
            //Get user map
            HashMap tailgateMap = (HashMap) entry.getValue();
            //Get phone field and append to list
            Tailgate myTailgate = new Tailgate(tailgateMap);
            myTailgate.setTailgateIdentifier(entry.getKey());
            tL.add(myTailgate);
        }
        tailgateList = tL;
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
                Intent nextScreen = new Intent(v.getContext(), TailgateMapActivity.class);
                nextScreen.putExtra("TAILGATE_LIST", tailgateList);
                startActivityForResult(nextScreen, 0);
                //transitionTailgateMapActivity();
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
                Intent nextScreen = new Intent(v.getContext(), TailgateHistoryActivity.class);
                nextScreen.putExtra("TAILGATE_LIST", tailgateList);
                startActivityForResult(nextScreen, 0);
            }
        });
    }
}