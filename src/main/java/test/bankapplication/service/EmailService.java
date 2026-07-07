package test.bankapplication.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class EmailService {
    private JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTransferNotification(String toMail, BigDecimal amount){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toMail);

        message.setSubject("NeoBank - Transfer Notification");
        message.setText(
                "Hello,\n\n" +
                        "A transfer of $" + amount + " has been completed from your account.\n\n" +
                        "If you did not authorize this transaction, please contact support immediately.\n\n" +
                        "Thank you,\nNeoBank Team"
        );
        message.setFrom("noreply@neobank.com");
        mailSender.send(message);
    }

    public void sendDepositNotification(String toEmail, BigDecimal amount){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("NeoBank - Money Received");
        message.setText(
                "Hello,\n\n" +
                        "You have received $" + amount + " into your account.\n\n" +
                        "Log in to view your updated balance.\n\n" +
                        "Thank you,\nNeoBank Team");
        message.setFrom("noreply@neobank.com");
        mailSender.send(message);
    }

}
