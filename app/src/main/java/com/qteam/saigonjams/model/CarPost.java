package com.qteam.saigonjams.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CarPost {

    private String userName;
    private String phoneNumber;
    private String startPosition;
    private String endPosition;
    private String vehicleType;
    private String date;

    public CarPost() {
    }

    public CarPost(String userName, String phoneNumber, String startPosition, String endPosition, String vehicleType, String date) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.vehicleType = vehicleType;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public String getEndPosition() {
        return endPosition;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getDate() {
        return date;
    }

}
