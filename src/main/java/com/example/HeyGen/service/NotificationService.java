package com.example.HeyGen.service;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class NotificationService {
    public static String NO_REPLY_EMAIL = "noreplyfarmwiseinterview@gmail.com";
    private JavaMailSenderImpl mailSender;

    List<SimpleMailMessage> sentMail;

    public NotificationService() {
        sentMail = new ArrayList<>();
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(NO_REPLY_EMAIL);
        mailSender.setPassword("fslycntatrewludb");
        // Set other properties if necessary
        // Configure properties for JavaMail
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
    }

    public void sendEmail(String email, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(NO_REPLY_EMAIL);
            message.setTo(email);
            message.setText(body);
            message.setSubject(subject);
            mailSender.send(message);
            System.out.println("Mail Send...");
            sentMail.add(message);
        } catch (MailException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public List<SimpleMailMessage> getSentMail() {
        return sentMail;
    }
}
