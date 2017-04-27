package com.example.brandonvowell.fanbase;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TailgateDetailActivity extends FragmentActivity {

    Tailgate currentTailgate;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tailgate_detail);
        TextView tailgateName = (TextView) findViewById(R.id.tailgate_name_textview);
        TextView tailgateDescription = (TextView) findViewById(R.id.description_textview);
        TextView thingsToBring = (TextView) findViewById(R.id.thingsToBring_textView);
        LinearLayout layout = (LinearLayout) findViewById(R.id.gallery_linear_layout);

        currentTailgate = (Tailgate) getIntent().getSerializableExtra("TAILGATE_OBJECT");
        storage = FirebaseStorage.getInstance();

        String name = currentTailgate.tailgateName;
        String description = currentTailgate.tailgateDescription;
        String things = currentTailgate.tailgateThingsToBring;
        tailgateName.setText(name);
        tailgateDescription.setText(description);
        thingsToBring.setText(things);

        //GET IMAGES
        List<String> urlsList = Arrays.asList(currentTailgate.imageURLS.split(","));
        for (String url : urlsList) {
            ImageView view = new ImageView(this);
            StorageReference storageReference = storage.getReference().child(url);
            Glide.with(this /* context */)
                    .using(new FirebaseImageLoader())
                    .load(storageReference)
                    .into(view);
            layout.addView(view);
        }
        System.out.println("hi");

    }
}
