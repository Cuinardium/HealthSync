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

    @Override
    public void sendAppointmentRequestMail(String clientEmail, String doctorEmail, String clientName, String healthCare, String date, String description) {

        String to = doctorEmail;
        String subject = "TurnosYa - Solicitud de turno";

        // Create mail body
        StringBuilder body = new StringBuilder();

        body.append("El usuario ").append(clientName).append(" ha solicitado un turno de ser posible en la siguiente fecha: ");
        body.append(date).append("\n\n");

        body.append("Descripcion:\n").append(description).append("\n\n");

        body.append("Datos del paciente:").append("\n\n");
        body.append("Nombre: ").append(clientName).append('\n');
        body.append("Obra social: ").append(healthCare).append('\n');
        body.append("Email: ").append(clientEmail).append('\n');
        
        sendMail(to, subject, body.toString());
    }
}
