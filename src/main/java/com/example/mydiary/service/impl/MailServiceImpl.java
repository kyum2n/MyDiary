package com.example.mydiary.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.mydiary.service.MailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendMail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("mydiary3947@gmail.com"); // 실제 발신자 이메일
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("[메일 전송 오류]");
            e.printStackTrace();
            throw new RuntimeException("메일 전송 실패: " + e.getMessage());
        }
    }
}
