package com.example.brandonvowell.fanbase;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import im.delight.android.location.SimpleLocation;

public class TailgateInfoActivity extends AppCompatActivity {

    private static final String TAG = "GPS Fetch Tag";
    private static final int PHOTO_UPLOAD_LIMIT = 5;
    private static final int REQUEST_CODE_PICKER = 1995;

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
    private Button btnUploadPhotos;

    private SimpleLocation location;
    DatabaseReference database;
    FirebaseStorage storage;


    private double latitude;
    private double longitude;
    private String tailgateName;
    private String tailgateDescription;
    private String tailgateThingsToBring;
    private String startTime;
    private String endTime;
    private int tailgateIsPublic;
    private int tailgateIsHome;
    private ArrayList<Image> tailgateImages;
    private String tailgateIdentifier;
    private String imageURLS =  "";

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
        btnUploadPhotos = (Button) findViewById(R.id.btnUploadPhotos);

        location = new SimpleLocation(this);
        database = FirebaseDatabase.getInstance().getReference().child("Tailgates");
        storage = FirebaseStorage.getInstance();

        tailgateImages = new ArrayList<>();

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

        btnUploadPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhotosPressed();
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
        //TODO: show and hide loading spinner before and after
        this.latitude = location.getLatitude();
        this.longitude = location.getLongitude();
        Toast.makeText(TailgateInfoActivity.this, "Location has been updated.",
                Toast.LENGTH_SHORT).show();
    }

    private void createTailgatePressed() {
        if (verifyUserInput()) {
            //Create tailgate object and push to firebase
            //TODO: show and hide loading spinner here
            Tailgate newTailgate = new Tailgate(latitude, longitude, tailgateName, tailgateDescription,
                    tailgateThingsToBring, startTime, endTime, tailgateIsPublic, tailgateIsHome);
            newTailgate.imageURLS = this.imageURLS;
            DatabaseReference newRef = database.push();
            //TODO: push saved images to firebase
            newRef.setValue(newTailgate);
            tailgateIdentifier = newRef.getKey();
            newTailgate.tailgateIdentifier = this.tailgateIdentifier;
            if (!tailgateImages.isEmpty()) {
                uploadImages(newTailgate);
            } else {
                newTailgate.imageURLS = null;
                transitionTailgateDetailScreen(newTailgate);
            }
        }
        else {
            Toast.makeText(TailgateInfoActivity.this, R.string.tailgate_creation_failed,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPhotosPressed() {
        ImagePicker.create(this).returnAfterFirst(false).limit(PHOTO_UPLOAD_LIMIT).start(REQUEST_CODE_PICKER);
    }

    private void uploadImages(Tailgate currentTailgate) {
        UploadTask uploadTask;
        for (Image image : tailgateImages) {
            String filepath = image.getPath();
            Uri file = Uri.fromFile(new File(filepath));
            String filepathString = tailgateIdentifier+"/"+file.getLastPathSegment();
            StorageReference riversRef = storage.getReference().child(filepathString);
            this.imageURLS += filepathString + ",";
            uploadTask = riversRef.putFile(file);

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });
        }
        currentTailgate.imageURLS = this.imageURLS;
        HashMap<String, Object> updateHashMap = new HashMap<>();
        updateHashMap.put("imageURLS", this.imageURLS);
        database.child(tailgateIdentifier).updateChildren(updateHashMap);
        transitionTailgateDetailScreen(currentTailgate);
    }

    private void transitionTailgateDetailScreen(Tailgate currentTailgate) {
        Intent nextScreen = new Intent(this, TailgateDetailActivity.class);
        nextScreen.putExtra("TAILGATE_OBJECT", currentTailgate);
        startActivityForResult(nextScreen, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            tailgateImages = (ArrayList<Image>) ImagePicker.getImages(data);
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