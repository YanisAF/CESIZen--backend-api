package com.example.CESIZen.service.email;

import com.example.CESIZen.model.user.User;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender,
                        SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendResetPinEmail(User user, String pin) {

        Context context = new Context();
        context.setVariable("name", user.getEmail());
        context.setVariable("pin", pin);
        context.setVariable("expiration", "5 minutes");

        String html = templateEngine.process(
                "reset-password-mail",
                context
        );

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Réinitialisation de votre mot de passe");
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Erreur envoi email", e);
        }
    }

    public void sendConfirmationReset(User user) {
        Context context = new Context();
        context.setVariable("name", user.getUsername());
        String html = templateEngine.process(
                "confirmation-reset-password",
                context
        );

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Votre mot de passe à bien été réinitialisé, " +
                    "si vous n'êtes pas l'origine de cette opération, veuillez nous contacter");
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Erreur envoi email", e);
        }
    }

    public void sendNotifAuthentication(User user, String ip, String device){
        Context context = new Context();
        context.setVariable( "name", user.getUsername());
        context.setVariable("date", user.getLastActivityAt());
        context.setVariable("ip", ip);
        context.setVariable("device", device);

        String html = templateEngine.process(
                "notification-authentication",
                context
        );

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setSubject("Si vous n'êtes pas l'origine de cette connexion, veuillez nous contacter");
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Erreur envoi email", e);
        }

    }

    public void sendNotifRegister(User user){
        Context context = new Context();
        context.setVariable("name", user.getUsername());

        String html = templateEngine.process(
                "confirmation-account-creation",
                context
        );

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true);

            helper.setTo(user.getEmail());
            helper.setText(html, true);

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Erreur envoi email", e);
        }

    }
}
