package com.example.CESIZen.configuration.event;

import com.example.CESIZen.model.event.LoginNotification;
import com.example.CESIZen.model.event.RegisterNotification;
import com.example.CESIZen.model.reset.ResetPasswordConfirmation;
import com.example.CESIZen.model.reset.ResetPasswordEvent;
import com.example.CESIZen.service.email.EmailService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EmailResetListener {

    private final EmailService emailService;

    public EmailResetListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @EventListener
    public void handle(ResetPasswordEvent event) {

        if ("EMAIL".equalsIgnoreCase(event.channel())) {
            emailService.sendResetPinEmail(
                    event.user(),
                    event.rawPin()
            );
        }
    }

    @EventListener
    public void handleConfirmation(ResetPasswordConfirmation event){
        if ("EMAIL".equalsIgnoreCase(event.channel())){
            emailService.sendConfirmationReset(
                    event.user()
            );
        }
    }

    @EventListener
    public void sendAuthNotification(LoginNotification userEvent){
        if ("EMAIL".equalsIgnoreCase(userEvent.channel())){
            emailService.sendNotifAuthentication(userEvent.user(), userEvent.ip(), userEvent.device());
        }
    }

    @EventListener
    public void sendCreationAccount(RegisterNotification userEvent){
        if ("EMAIL".equalsIgnoreCase(userEvent.channel())){
            emailService.sendNotifRegister(userEvent.user());
        }
    }
}
