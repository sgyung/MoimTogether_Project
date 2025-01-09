package com.project.moimtogether.service.member;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    private static final String SENDER_EMAIL = "tjsrud2907@gmail.com";
    private static final int TEMP_PASSWORD_LENGTH = 8;
    private static final String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";


    public void sendMail(String to, String title, String content) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject(title);
        helper.setText(content, true);
        helper.setReplyTo(SENDER_EMAIL);
        try {
            javaMailSender.send(message);
        } catch (RuntimeException e) {
            throw new RuntimeException("메일 전송 시 오류 발생하였습니다.",e);
        }
    }

    // 임시 비밀번호 생성
    public String createTemporaryPassword() {
        StringBuilder password = new StringBuilder(TEMP_PASSWORD_LENGTH);
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < TEMP_PASSWORD_LENGTH; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            password.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }

        return password.toString();
    }
}
