package com.example.mydiary.service;

public interface MailService {
    void sendMail(String to, String subject, String text);

}