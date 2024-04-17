package com.example.HeyGen;

import com.example.HeyGen.model.Status;
import com.example.HeyGen.model.VideoModel;
import com.example.HeyGen.service.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VideoProcessorTests {
	@Autowired
	VideoManager videoManager;
	@Autowired
	VideoService videoService;
	@Autowired
	NotificationService notificationService;
	//this is the email that will get sent the notification when completed
	String email = "insert-test-email@gmail.com";

	Thread videoProcessorThread;
	Thread videoErrorProcessorThread;

	@BeforeEach
	void setup() {
		videoProcessorThread = new Thread(new VideoProcessorThread(videoManager, notificationService,videoManager.getQueue(), videoManager.getDLQ()));
		videoProcessorThread.start();
		videoErrorProcessorThread = new Thread(new DLQService(videoManager.getQueue(), videoManager.getDLQ(), videoManager, notificationService));
		videoErrorProcessorThread.start();
	}

	@AfterEach
	void cleanup() {
		videoErrorProcessorThread.interrupt();
		videoProcessorThread.interrupt();
		notificationService.getSentMail().clear();
	}

	@Test
	void whenUploadVideo_thenUpdateStatusUponCompletion() throws InterruptedException {
		VideoModel vm1 = new VideoModel(1, Status.PENDING, email, 1000);
		videoManager.uploadVideo(vm1);

		assertThat(videoService.getStatusWithID(vm1.getId()).getResult(), is(Status.PENDING));

		Thread.sleep(5000);

		assertThat(videoService.getStatusWithID(vm1.getId()).getResult(), is(Status.COMPLETED));
		System.out.println("Vm1 Status " + videoService.getStatusWithID(vm1.getId()).getResult());
		Thread.sleep(6000);
		assertThat(notificationService.getSentMail().size(), is(1));
	}


}
