package ar.edu.itba.paw.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import ar.edu.itba.paw.models.User;

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

    @Override
    public void sendAppointmentRequestMail(User client, User doctor, List<String> possibleDates, String reason) {

        String to = doctor.getEmail();
        String subject = "TurnosYa - Solicitud de turno";

        // Create mail body
        StringBuilder body = new StringBuilder();

        body.append("El usuario ").append(client.getEmail()).append(" ha solicitado un turno de ser posible en las siguientes fechas:\n");
        for (String date : possibleDates) {
            body.append(date).append('\n');
        }

        body.append("Motivo:\n");
        body.append(reason);

        sendMail(to, subject, body.toString());
    }
}
