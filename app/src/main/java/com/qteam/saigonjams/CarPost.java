package com.qteam.saigonjams;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CarPost {

    private String userName;
    private String phoneNumber;
    private String startPosition;
    private String endPosition;
    private String vehicleType;

    public CarPost() {
    }

    public CarPost(String name, String phone, String startPos, String endPos, String vehicleType) {
        this.userName = name;
        this.phoneNumber = phone;
        this.startPosition = startPos;
        this.endPosition = endPos;
        this.vehicleType = vehicleType;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
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
}
