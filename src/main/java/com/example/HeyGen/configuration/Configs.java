package com.example.HeyGen.configuration;

import com.example.HeyGen.model.VideoModel;
import com.example.HeyGen.service.NotificationService;
import com.example.HeyGen.service.VideoManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.Queue;

@Configuration
public class Configs {

    @Bean
    public Queue<VideoModel> deadLetterQueue() {
        return new LinkedList<>();
    }

    @Bean
    public NotificationService notificationService() {
        return new NotificationService();
    }

    @Bean
    public VideoManager videoManager(NotificationService notificationService) {
        return new VideoManager(notificationService);
    }
}
