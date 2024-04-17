package com.example.HeyGen.controller;

import com.example.HeyGen.model.StatusResponse;
import com.example.HeyGen.model.VideoModel;
import com.example.HeyGen.model.VideoUploadRequestBody;
import com.example.HeyGen.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.KeyStoreException;

@RestController
public class VideoController {
    @Autowired
    VideoService videoService;

    @GetMapping("/video/status/{id}")
    StatusResponse getVideoStatus(@PathVariable int id) throws KeyStoreException {
        return videoService.getStatusWithID(id);
    }

    @PostMapping("/video/upload")
    VideoModel uploadVideo(@RequestBody VideoUploadRequestBody videoModel) {
        return videoService.uploadVideo(videoModel);
    }
}
