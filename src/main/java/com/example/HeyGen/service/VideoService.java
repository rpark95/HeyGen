package com.example.HeyGen.service;

import com.example.HeyGen.model.StatusResponse;
import com.example.HeyGen.model.VideoModel;
import com.example.HeyGen.model.VideoUploadRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VideoService {
    @Autowired
    VideoManager videoManager;

    @Autowired
    public VideoService(VideoManager videoManager) {
        this.videoManager = videoManager;
    }
    public StatusResponse getStatusWithID(int videoId) {
        Optional<VideoModel> videoModel = videoManager.getVideoModel(videoId);
        if (!videoModel.isPresent()) {
            throw new RuntimeException();
        }
        StatusResponse response = new StatusResponse(videoModel.get().getStatus());
        return response;
    }

    public VideoModel uploadVideo(VideoUploadRequestBody videoModel) {
        return videoManager.uploadVideo(videoModel);
    }
}
