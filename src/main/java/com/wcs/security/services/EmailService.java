package com.wcs.security.services;

public interface EmailService {
    void sendEmail(String toUser, String subject, String body);
}
