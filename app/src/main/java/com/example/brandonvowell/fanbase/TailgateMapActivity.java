package com.example.brandonvowell.fanbase;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import im.delight.android.location.SimpleLocation;


public class TailgateMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
GoogleMap.OnMapClickListener {

    private GoogleMap mMap;

    private SimpleLocation location;
    private double latitude;
    private double longitude;

    private BottomSheetBehavior mBottomSheetBehavior1;

    DatabaseReference database;

    private ArrayList<Tailgate> tailgateList;

    Tailgate currentTailgate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailgate_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        View bottomSheet = findViewById(R.id.map_preview);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior1.setHideable(true);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);

        final Button bottomSheetButton = (Button) findViewById(R.id.bottomSheetButton);

        location = new SimpleLocation(this);
        database = FirebaseDatabase.getInstance().getReference().child("Tailgates");
        tailgateList = (ArrayList<Tailgate>) getIntent().getSerializableExtra("TAILGATE_LIST");

        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        bottomSheetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextScreen = new Intent(v.getContext(), TailgateDetailActivity.class);
                nextScreen.putExtra("TAILGATE_OBJECT", currentTailgate);
                startActivityForResult(nextScreen, 0);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        mMap.setMyLocationEnabled(true);
        getCurrentDeviceLocation();
        LatLng myLocation = new LatLng(this.latitude, this.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17.0f));
        populateMap();

    }

    private void getCurrentDeviceLocation() {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    private void populateMap() {
        for (Tailgate tailgate : tailgateList) {
            LatLng pinLocation = new LatLng(tailgate.getLatitude(), tailgate.getLongitude());
            BitmapDescriptor pinColor;
            if (tailgate.getTailgateIsHome() == 1) {
                pinColor = BitmapDescriptorFactory.defaultMarker(12.00f);
            } else {
                pinColor = BitmapDescriptorFactory.defaultMarker(231.00f);
            }
            if(mMap != null) {
                mMap.addMarker(new MarkerOptions()
                        .position(pinLocation).title(tailgate.getStartTime()).icon(pinColor)).setTag(tailgate);
            }
            else {
                System.out.println("MAP IS NULL");
            }
            mMap.setOnMarkerClickListener(this);
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        currentTailgate = (Tailgate) marker.getTag();
        String tailgateName = currentTailgate.tailgateName;
        String tailgateDesc = currentTailgate.tailgateDescription;
        String startTime = "Starts: " + currentTailgate.startTime;
        String endTime = "Ends: " + currentTailgate.endTime;
        if(mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            TextView tname = (TextView) findViewById(R.id.tailgateName);
            TextView tdesc = (TextView) findViewById(R.id.tailgateDescription);
            TextView stime = (TextView) findViewById(R.id.startTime);
            TextView etime = (TextView) findViewById(R.id.endTime);
            tname.setText(tailgateName);
            tdesc.setText(tailgateDesc);
            stime.setText(startTime);
            etime.setText(endTime);
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        else {
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        return false;
    }

    @Override
    public void onMapClick (LatLng point) {
        if (mBottomSheetBehavior1.getState() != 5) {
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
            mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
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
}