package com.pedrodisanti.libraryapi.service.impl;

import com.pedrodisanti.libraryapi.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Override
    public void sendMails(String message, List<String> mailsList) {
        String[] mails = mailsList.toArray(new String[mailsList.size()]);
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        String sender = "mail@library-api.com";
        mailMessage.setFrom(sender);
        mailMessage.setSubject("Book with late loan.");
        mailMessage.setText(message);
        mailMessage.setTo(mails);

        javaMailSender.send(mailMessage);
    }
}
