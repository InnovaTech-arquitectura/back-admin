package com.innovatech.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException; // Cambiar a jakarta.mail
import jakarta.mail.internet.MimeMessage; // Cambiar a jakarta.mail

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendRecoveryCode(String to, String recoveryCode) {
        sendEmail(to, "Password Recovery Code", "Your recovery code is: " + recoveryCode);
    }

    public void sendPasswordChangedConfirmation(String to) {
        sendEmail(to, "Password Changed", "Your password has been successfully updated.");
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending email", e);
        }
    }
}
