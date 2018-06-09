package com.qteam.saigonjams.model;

public class AlertPost {
    private String position;
    private String status;
    private String date;
    private String imageURL;

    public AlertPost() {
    }

    public AlertPost(String position, String status, String date, String imageURL) {
        this.position = position;
        this.status = status;
        this.date = date;
        this.imageURL = imageURL;
    }

    public String getPosition() {
        return position;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public String getImageURL() {
        return imageURL;
    }

}
