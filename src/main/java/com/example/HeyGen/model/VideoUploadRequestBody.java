package com.example.HeyGen.model;

public class VideoUploadRequestBody {
    String email;
    String videoLink;


    public VideoUploadRequestBody(String email, String videoLink) {
        this.email = email;
        this.videoLink = videoLink;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }
}
