package com.example.brandonvowell.fanbase;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import im.delight.android.location.SimpleLocation;

public class TailgateDetailActivity extends FragmentActivity implements OnMapReadyCallback {

    Tailgate currentTailgate;
    FirebaseStorage storage;

    public LinearLayout layout;
    ImageView coverPhotoView;

    private GoogleMap mMap;

    private SimpleLocation location;


    private double latitude;
    private double longitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailgate_detail);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detail_map);
        mapFragment.getMapAsync(this);

        TextView tailgateName = (TextView) findViewById(R.id.tailgate_name_textview);
        TextView tailgateDescription = (TextView) findViewById(R.id.description_textview);
        TextView thingsToBring = (TextView) findViewById(R.id.thingsToBring_textView);
        coverPhotoView = (ImageView) findViewById(R.id.cover_image_view);

        layout = (LinearLayout) findViewById(R.id.gallery_linear_layout);

        currentTailgate = (Tailgate) getIntent().getSerializableExtra("TAILGATE_OBJECT");
        storage = FirebaseStorage.getInstance();

        location = new SimpleLocation(this);

        String name = currentTailgate.tailgateName;
        String description = currentTailgate.tailgateDescription;
        String things = currentTailgate.tailgateThingsToBring;
        tailgateName.setText(name);
        tailgateDescription.setText(description);
        thingsToBring.setText(things);

        //GET IMAGES
        //TODO Only load images if urlsList is not empty here
        List<String> urlsList = Arrays.asList(currentTailgate.imageURLS.split(","));
        if (!urlsList.isEmpty()) {
            loadCoverPhoto(urlsList.get(0));
        }
        for (String url : urlsList) {
//            ImageView view = new ImageView(this);
//            StorageReference storageReference = storage.getReference().child(url);
//            Glide.with(this /* context */)
//                    .using(new FirebaseImageLoader())
//                    .load(storageReference)
//                    .into(view);
//            layout.addView(view);

            //ATTEMPT 2
            storage.getReference().child(url).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    downloadImage(uri.toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //handle errors here
                }
            });
        }
        System.out.println("hi");

    }

    private void loadCoverPhoto(String coverURL) {
        coverPhotoView.setScaleType(ImageView.ScaleType.FIT_XY);
        coverPhotoView.setAdjustViewBounds(true);
        Picasso.with(getApplicationContext()).load(coverURL).into(coverPhotoView);
    }

    private void downloadImage(String downloadURL) {
        ImageView view = new ImageView(this);
        Picasso.with(getApplicationContext()).load(downloadURL).into(view);
        view.setAdjustViewBounds(true);
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setPadding(20, 20, 20, 20);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPhoto((ImageView) v, 200, 200);
            }
        });
        layout.addView(view);
    }

    private void loadPhoto(ImageView imageView, int width, int height) {

        ImageView tempImageView = imageView;


        AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                (ViewGroup) findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        image.setImageDrawable(tempImageView.getDrawable());
        imageDialog.setView(layout);
        imageDialog.setPositiveButton("Close", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });


        imageDialog.create();
        imageDialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        getCurrentDeviceLocation();
        LatLng myLocation = new LatLng(this.latitude, this.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17.0f));
        LatLng pinLocation = new LatLng(currentTailgate.latitude, currentTailgate.longitude);
        BitmapDescriptor pinColor;
        if (currentTailgate.tailgateIsHome == 1) {
            pinColor = BitmapDescriptorFactory.defaultMarker(12.00f);
        } else {
            pinColor = BitmapDescriptorFactory.defaultMarker(231.00f);
        }
        mMap.addMarker(new MarkerOptions()
                .position(pinLocation).title(currentTailgate.startTime).icon(pinColor)).setTag(currentTailgate);
    }

    private void getCurrentDeviceLocation() {
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
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
