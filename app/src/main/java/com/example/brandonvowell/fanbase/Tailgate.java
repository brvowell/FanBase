package com.example.brandonvowell.fanbase;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.Math;

public class Tailgate implements Serializable {
    public double latitude;
    public double longitude;
    public String tailgateName;
    public String tailgateDescription;
    public String tailgateThingsToBring;
    public String startTime;
    public String endTime;
    public long tailgateIsPublic;
    public long tailgateIsHome;
    public String tailgateIdentifier;
    public String imageURLS;

    public Tailgate (double latitude, double longitude, String name, String description, String thingsToBring,
                    String startTime, String endTime, int isPublic, int isHome) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.tailgateName = name;
        this.tailgateDescription = description;
        this.tailgateThingsToBring = thingsToBring;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tailgateIsPublic = isPublic;
        this.tailgateIsHome = isHome;

    }

    public Tailgate(HashMap<String, Object> map) {
        this.latitude = (double) map.get("latitude");
        this.longitude = (double) map.get("longitude");
        this.tailgateName = (String) map.get("tailgateName");
        this.tailgateDescription = (String) map.get("tailgateDescription");
        this.tailgateThingsToBring = (String) map.get("tailgateThingsToBring");
        this.startTime = (String) map.get("startTime");
        this.endTime = (String) map.get("endTime");
        this.tailgateIsPublic = (long) map.get("tailgateIsPublic");
        this.tailgateIsHome = (long) map.get("tailgateIsHome");
    }

    public String getTailgateName() {
        return this.tailgateName;
    }
    public String getStartTime() { return this.startTime; }
    public double getLatitude() { return this.latitude; }
    public double getLongitude() { return this.longitude; }
    public long getTailgateIsHome() { return this.tailgateIsHome; }
    public void setTailgateIdentifier(String s) { this.tailgateIdentifier = s; }
}
