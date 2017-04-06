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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

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

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager locationManager;
    private LocationRequest mLocationRequest;

    private SimpleLocation location;

    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000;

    private double latitude;
    private double longitude;

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
        //startLocationUpdates();
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        System.out.print(latitude);
        System.out.print(longitude);
    }

    private void createTailgatePressed() {

    }

    @Override
    public void onStart() {
        super.onStart();
//        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (mGoogleApiClient.isConnected()) {
//            mGoogleApiClient.disconnect();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        location.beginUpdates();
    }

    @Override
    protected void onPause() {
        location.endUpdates();
        super.onPause();
    }

    //GPS METHODS BELOW
//    @Override
//    public void onConnected(Bundle bundle) {
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        } startLocationUpdates();
//        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        if(mLocation == null){
//            startLocationUpdates();
//        }
//        if (mLocation != null) {
//            double latitude = mLocation.getLatitude();
//            double longitude = mLocation.getLongitude();
//        } else {
//            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    protected void startLocationUpdates() {
//        // Create the location request
//        mLocationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(UPDATE_INTERVAL)
//                .setFastestInterval(FASTEST_INTERVAL);
//        // Request location updates
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
//                mLocationRequest, this);
//        Log.d("reque", "--->>>>");
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.i(TAG, "Connection Suspended");
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//
//    }
}