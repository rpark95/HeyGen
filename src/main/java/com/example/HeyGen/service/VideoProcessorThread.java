package com.example.HeyGen.service;

import com.example.HeyGen.model.Status;
import com.example.HeyGen.model.VideoModel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class VideoProcessorThread implements Runnable{
    Queue<VideoModel> queue;
    Queue<VideoModel> dlq;

    Map<Integer, Integer> dlqCount;
    @Autowired
    VideoManager videoManager;

    int MAX_RETRIES = 5;

    @Autowired
    NotificationService notificationService;

    public VideoProcessorThread(VideoManager videoManager, NotificationService notificationService, Queue<VideoModel> queue, Queue<VideoModel> dlq) {
        this.videoManager = videoManager;
        this.notificationService = notificationService;
        this.queue = queue;
        this.dlq = dlq;
        dlqCount = new HashMap<>();
    }

    @Override
    public void run() {
        while (true) {
            while (!queue.isEmpty()) {
                VideoModel videoModel = queue.poll();
                try {
                    System.out.println("Processing video");
                    Thread.sleep(videoModel.getUploadTime());//video processing
                    videoModel.setStatus(Status.COMPLETED);
                    videoManager.setKeyValueInMap(videoModel.getId(), videoModel);
                    System.out.println("Video completed processing");
                    String subject = "Video Upload Status";
                    String body = "Your video has finished uploading. Status " + videoModel.getStatus() + " " + "VideoId: " + videoModel.getId();
                    notificationService.sendEmail(videoModel.getEmail(), subject, body);
                } catch (NullPointerException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    int count = 0;
                    if (dlqCount.containsKey(videoModel.getId())) {
                        count = dlqCount.get(videoModel.getId());
                    }
                    if (count < MAX_RETRIES) {
                        dlq.add(videoModel);
                        dlqCount.put(videoModel.getId(), count+1);
                    } else {
                        videoModel.setStatus(Status.ERROR);
                        videoManager.setKeyValueInMap(videoModel.getId(), videoModel);
                        String subject = "Video Upload Status";
                        String body = "Your video has finished uploading. Status " + videoModel.getStatus() + " " + "VideoId: " + videoModel.getId();
                        notificationService.sendEmail(videoModel.getEmail(), subject, body);
                        System.out.println("sending email");
                    }
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
