package ar.edu.itba.paw.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class MailServiceImpl implements MailService {
    
    private static final String FROM = "noreply@turnosya.com";

    private final JavaMailSender mailSender;


    @Autowired
    public MailServiceImpl(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    private void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(FROM);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

}
