package backend343.service;

import backend343.logger.LoggerSingleton;
import backend343.models.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender emailSender;

    private static final LoggerSingleton logger = LoggerSingleton.getInstance();


    public void sendVerificationEmail(String to, String subject, String text) {
        logger.logInfo("Sending verification email to " + to);
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            emailSender.send(message);
            logger.logInfo("Verification email sent successfully to " + to);
        } catch (MessagingException e) {
            logger.logError("Error sending verification email to " + to + ": " + e.getMessage());
        }
    }

    public void sendEmail(String to, String subject, String text) {
        logger.logInfo("Sending email to " + to);
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true);

            emailSender.send(message);
            logger.logInfo("Email sent successfully to " + to);
        } catch (MessagingException e) {
            logger.logError("Error sending email to " + to + ": " + e.getMessage());
        }
    }


}