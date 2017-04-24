package com.example.brandonvowell.fanbase;

import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import im.delight.android.location.SimpleLocation;


public class TailgateMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    private SimpleLocation location;
    private double latitude;
    private double longitude;

    private BottomSheetBehavior mBottomSheetBehavior1;

    DatabaseReference database;

    private ArrayList<Tailgate> tailgateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailgate_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        View bottomSheet = findViewById(R.map_bottom_sheet);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);

        location = new SimpleLocation(this);
        database = FirebaseDatabase.getInstance().getReference().child("Tailgates");
        tailgateList = new ArrayList<Tailgate>();
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


        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        populateMap();
    }

    private void collectTailgates(Map<String,Object> tailgates) {
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : tailgates.entrySet()){

            //Get user map
            HashMap tailgateMap = (HashMap) entry.getValue();
            //Get phone field and append to list
            Tailgate myTailgate = new Tailgate(tailgateMap);
            tailgateList.add(myTailgate);
        }
        populateMap();
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
        mMap.setMyLocationEnabled(true);
        getCurrentDeviceLocation();
        LatLng myLocation = new LatLng(this.latitude, this.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17.0f));
    }

    private void getCurrentDeviceLocation() {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
    }

    private void populateMap() {
        for (Tailgate tailgate : tailgateList) {
            LatLng pinLocation = new LatLng(tailgate.latitude, tailgate.longitude);
            mMap.addMarker(new MarkerOptions()
                .position(pinLocation).title(tailgate.startTime)).setTag(tailgate);
            mMap.setOnMarkerClickListener(this);
        }
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        Tailgate tail = (Tailgate) marker.getTag();
        String tailgateName = tail.tailgateName;
        Toast.makeText(TailgateMapActivity.this,
                tailgateName,
                Toast.LENGTH_SHORT).show();
        return false;
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