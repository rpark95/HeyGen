package com.example.HeyGen.model;

public class StatusResponse {
    Status result;
    public StatusResponse(Status status) {
        result = status;
    }

    public Status getResult() {
        return result;
    }

    public void setResult(Status result) {
        this.result = result;
    }
}
