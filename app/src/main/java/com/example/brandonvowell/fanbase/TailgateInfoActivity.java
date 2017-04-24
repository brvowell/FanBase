package com.example.brandonvowell.fanbase;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import im.delight.android.location.SimpleLocation;

public class TailgateInfoActivity extends AppCompatActivity {

    private static final String TAG = "GPS Fetch Tag";
    private EditText nameTextField;
    private EditText descriptionTextField;
    private EditText thingsToBringTextField;
    private EditText startTimeTextField;
    private EditText endTimeTextField;
    private RadioButton publicRadioBtn;
    private RadioButton privateRadioBtn;
    private RadioButton homeRadioBtn;
    private RadioButton awayRadioBtn;
    private Button btnSetLocation;
    private Button btnCreateTailgate;

    private SimpleLocation location;
    DatabaseReference database;

    private double latitude;
    private double longitude;
    private String tailgateName;
    private String tailgateDescription;
    private String tailgateThingsToBring;
    private String startTime;
    private String endTime;
    private int tailgateIsPublic;
    private int tailgateIsHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailgate_info);
        nameTextField = (EditText) findViewById(R.id.nameTextField);
        descriptionTextField = (EditText) findViewById(R.id.descriptionTextField);
        thingsToBringTextField = (EditText) findViewById(R.id.thingsToBringTextView);
        startTimeTextField = (EditText) findViewById(R.id.startTimeTextField);
        endTimeTextField = (EditText) findViewById(R.id.endTimeTextField);
        publicRadioBtn = (RadioButton) findViewById(R.id.publicRadBtn);
        privateRadioBtn = (RadioButton) findViewById(R.id.privateRadBtn);
        homeRadioBtn = (RadioButton) findViewById(R.id.homeRadBtn);
        awayRadioBtn = (RadioButton) findViewById(R.id.awayRadBtn);
        btnSetLocation = (Button) findViewById(R.id.btnSetLocation);
        btnCreateTailgate = (Button) findViewById(R.id.btnCreateTailgate);

        location = new SimpleLocation(this);
        database = FirebaseDatabase.getInstance().getReference().child("Tailgates");

        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        btnSetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTailgateLocationPressed();
            }
        });

        btnCreateTailgate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTailgatePressed();
            }
        });

    }

    private void setTailgateLocationPressed() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        getCurrentDeviceLocation();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.set_location_prompt)).setPositiveButton("Set Location", dialogClickListener)
                .setNegativeButton("Cancel", dialogClickListener).show();
    }

    private void getCurrentDeviceLocation() {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    private void createTailgatePressed() {
        if (verifyUserInput()) {
            //Create tailgate object and push to firebase
            Tailgate newTailgate = new Tailgate(latitude, longitude, tailgateName, tailgateDescription,
                    tailgateThingsToBring, startTime, endTime, tailgateIsPublic, tailgateIsHome);
            DatabaseReference newRef = database.push();
            newRef.setValue(newTailgate);


        }
        else {
            Toast.makeText(TailgateInfoActivity.this, R.string.tailgate_creation_failed,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean verifyUserInput() {
        //returns true if user correctly supplied all necessary tailgate info, else false
        this.tailgateName = nameTextField.getText().toString();
        this.tailgateDescription = descriptionTextField.getText().toString();
        this.tailgateThingsToBring = thingsToBringTextField.getText().toString();
        this.startTime = startTimeTextField.getText().toString();
        this.endTime = endTimeTextField.getText().toString();
        this.tailgateIsHome = getTailgateHomeStatus();
        this.tailgateIsPublic = getTailgatePrivacyStatus();

        if (tailgateName.isEmpty() || tailgateDescription.isEmpty() || tailgateThingsToBring.isEmpty() ||
                startTime.isEmpty() || endTime.isEmpty() || tailgateIsHome == -1 || tailgateIsPublic == -1) {
            return false;
        }
        return true;
    }

    private int getTailgateHomeStatus() {
        //returns 1 if home, 0 if away, -1 if neither
        if (homeRadioBtn.isChecked()) {
            return 1;
        } else if (awayRadioBtn.isChecked()) {
            return 0;
        } else {
            return -1;
        }
    }

    private int getTailgatePrivacyStatus() {
        //returns 1 if public, 0 if private, -1 if neither
        if (publicRadioBtn.isChecked()) {
            return 1;
        } else if (privateRadioBtn.isChecked()) {
            return 0;
        } else {
            return -1;
        }
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