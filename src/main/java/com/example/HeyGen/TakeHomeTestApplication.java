package com.example.HeyGen;

import com.example.HeyGen.service.DLQService;
import com.example.HeyGen.service.NotificationService;
import com.example.HeyGen.service.VideoManager;
import com.example.HeyGen.service.VideoProcessorThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TakeHomeTestApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(TakeHomeTestApplication.class, args);

		NotificationService notificationService = context.getBean(NotificationService.class);
		VideoManager videoManager = context.getBean(VideoManager.class);

		Thread videoProcessorThread = new Thread(new VideoProcessorThread(videoManager, notificationService,videoManager.getQueue(), videoManager.getDLQ()));
		videoProcessorThread.start();
		Thread videoErrorProcessorThread = new Thread(new DLQService(videoManager.getQueue(), videoManager.getDLQ(), videoManager, notificationService));
		videoErrorProcessorThread.start();
	}

}
