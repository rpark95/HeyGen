package com.example.HeyGen.model;

public class VideoModel {
    int id;
    Status status;

    String email;

    //configurable value meant for testing
    int uploadTime;

    public VideoModel(int id, Status status, String email, int uploadTime) {
        this.id = id;
        this.status = status;
        this.email = email;
        this.uploadTime = uploadTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getEmail() {
        return email;
    }

    public int getUploadTime() {
        return uploadTime;
    }
}
