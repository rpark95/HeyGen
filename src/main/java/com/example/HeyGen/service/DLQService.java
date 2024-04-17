package com.example.HeyGen.service;

import com.example.HeyGen.model.Status;
import com.example.HeyGen.model.VideoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

@Service
public class DLQService implements Runnable {
    Queue<VideoModel> dlQueue;
    Queue<VideoModel> queue;
    Map<Integer, Integer> dlqCount;
    VideoManager videoManager;
    NotificationService notificationService;


    int maxSize = 0;
    @Autowired
    public DLQService(Queue<VideoModel> queue, Queue<VideoModel> dlQueue, VideoManager videoManager, NotificationService notificationService) {
        this.queue = queue;
        this.dlQueue = dlQueue;
        this.videoManager = videoManager;
        this.notificationService = notificationService;
        dlqCount = new HashMap<>();
    }

    @Override
    public void run() {
        while (true) {
            while (!dlQueue.isEmpty()) {
                System.out.println("In dead letter queue");
                maxSize = Math.max(maxSize, dlQueue.size());
                VideoModel vm = dlQueue.poll();
                vm.setStatus(Status.PENDING);
                queue.add(vm);
            }
        }
    }
}
