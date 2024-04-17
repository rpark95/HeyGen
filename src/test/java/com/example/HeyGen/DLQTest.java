package com.example.HeyGen;

import com.example.HeyGen.model.Status;
import com.example.HeyGen.model.VideoModel;
import com.example.HeyGen.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
public class DLQTest {
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
    //vm1 will retry to process and eventually fail and stay as an error

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
    void whenError_thenInsertModelIntoDLQ() throws InterruptedException {
        VideoModel vm3 = new VideoModel(3, Status.PENDING, email, -1);
        videoManager.uploadVideo(vm3);
        assertThat(videoService.getStatusWithID(vm3.getId()).getResult(), is(Status.PENDING));
        System.out.println("Vm Status " + videoService.getStatusWithID(vm3.getId()).getResult());
        Thread.sleep(5000);
        assertThat(videoService.getStatusWithID(vm3.getId()).getResult(), is(Status.ERROR));
        System.out.println("Vm Status " + videoService.getStatusWithID(vm3.getId()).getResult());
        Thread.sleep(5000);
        assertThat(notificationService.getSentMail().size(), is(1));

    }
}
