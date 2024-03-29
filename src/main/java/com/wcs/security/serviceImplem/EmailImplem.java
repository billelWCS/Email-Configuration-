package com.wcs.security.serviceImplem;

import com.wcs.security.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailImplem implements EmailService {

    @Autowired
    JavaMailSender emailSender;

//    public EmailImplem(JavaMailSender emailSender) {
//        this.emailSender = emailSender;
//    }

    @Override
    public void sendEmail(String toUser, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toUser);
        message.setSubject(subject);
        message.setText(body);
        emailSender.send(message);
    }
}
