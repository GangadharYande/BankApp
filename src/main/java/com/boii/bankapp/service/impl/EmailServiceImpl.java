package com.boii.bankapp.service.impl;

import com.boii.bankapp.dto.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Override
    public void senderEmailAlert(EmailDetails emailDetails) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(senderEmail);
            mailMessage.setTo(emailDetails.getRecipients());
            mailMessage.setText(emailDetails.getMessageBody());
            mailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(mailMessage);
            System.out.println("Statement via Email sent successfully");
        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendEmailWithAttactment(EmailDetails emailDetails) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true); // true indicates multipart
            mimeMessageHelper.setFrom(senderEmail);
            mimeMessageHelper.setTo(emailDetails.getRecipients());
            mimeMessageHelper.setSubject(emailDetails.getSubject());
            mimeMessageHelper.setText(emailDetails.getMessageBody(), true); // true to enable HTML content

            // Verify that the attachment path is valid and the file exists
            File attachmentFile = new File(emailDetails.getAttachment());
            if (attachmentFile.exists()) {
                FileSystemResource fileResource = new FileSystemResource(attachmentFile);
                mimeMessageHelper.addAttachment(Objects.requireNonNull(fileResource.getFilename()), fileResource);
                log.info("Attachment: {} added successfully.", fileResource.getFilename());
            } else {
                log.warn("Attachment file not found: {}", emailDetails.getAttachment());
            }

            javaMailSender.send(mimeMessage);
            log.info("Email sent successfully to {}", String.join(", ", emailDetails.getRecipients()));
        } catch (MessagingException e) {
            log.error("Error sending email with attachment: ", e);
            throw new RuntimeException("Failed to send email", e);
        }
    }
}

