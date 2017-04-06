package com.example.brandonvowell.fanbase;

public class Tailgate {
    public double latitude;
    public double longitude;
    public String tailgateName;
    public String tailgateDescription;
    public String tailgateThingsToBring;
    public String startTime;
    public String endTime;
    public int tailgateIsPublic;
    public int tailgateIsHome;

    public Tailgate(double latitude, double longitude, String name, String description, String thingsToBring,
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
}
