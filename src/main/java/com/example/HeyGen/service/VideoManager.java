package com.example.HeyGen.service;

import com.example.HeyGen.model.Status;
import com.example.HeyGen.model.VideoModel;
import com.example.HeyGen.model.VideoUploadRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class VideoManager {
    Map<Integer, VideoModel> videoMap;
    //Thread videoProcessorThread;
    Queue<VideoModel> queue;
    Queue<VideoModel> deadLetterQueue;

    static int DEFAULT_UPLOAD_TIME = 1000;

    @Autowired
    NotificationService notificationService;
    @Autowired
    public VideoManager(NotificationService notificationService) {
        this.notificationService = notificationService;
        videoMap = new HashMap<>();
        queue = new LinkedList<>();
        deadLetterQueue = new LinkedList<>();

    }

    public VideoModel uploadVideo(VideoUploadRequestBody model) {
        VideoModel vm = new VideoModel(videoMap.size(), Status.PENDING, model.getEmail(), DEFAULT_UPLOAD_TIME);
        videoMap.put(vm.getId(), vm);
        queue.add(vm);
        return vm;
    }

    public VideoModel uploadVideo(VideoModel vm) {
        videoMap.put(vm.getId(), vm);
        queue.add(vm);
        return vm;
    }

    public void setKeyValueInMap(int key, VideoModel videoModel) {
        videoMap.put(key, videoModel);
    }

    public Map<Integer, VideoModel> getMap() {
        return videoMap;
    }

    public Optional<VideoModel> getVideoModel(int key) {
        if (!videoMap.containsKey(key)) {
            return null;
        }
        return Optional.of(videoMap.get(key));
    }
    public Queue<VideoModel> getQueue() {
        return queue;
    }

    public Queue<VideoModel> getDLQ() {
        return deadLetterQueue;
    }
}
