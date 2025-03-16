package com.example.toyauth.app.user.service;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final JavaMailSender mailSender;
    private final Random random = new Random();

    /**
     * 이메일 인증 코드 생성 및 전송
     */
    public void sendVerificationCode(String email) {
        String code = generateVerificationCode();
       //saveVerificationCode(email, code);
        sendEmail(email, code);
    }

    /**
     * 랜덤 6자리 인증 코드 생성
     */
    private String generateVerificationCode() {
        return String.format("%06d", random.nextInt(1000000));
    }

    /**
     * 이메일 전송
     */
    private void sendEmail(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("비밀번호 변경을 위한 인증 코드");
        message.setText("인증 코드: " + code + " (5분 내에 입력하세요)");
        mailSender.send(message);
    }
}
