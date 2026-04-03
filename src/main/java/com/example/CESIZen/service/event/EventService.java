package com.example.CESIZen.service.event;

import com.example.CESIZen.model.event.LoginNotification;
import com.example.CESIZen.model.event.RegisterNotification;
import com.example.CESIZen.model.reset.ResetPasswordConfirmation;
import com.example.CESIZen.model.reset.ResetPasswordEvent;
import com.example.CESIZen.model.user.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final ApplicationEventPublisher publisher;

    public EventService(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publisherResetEvent(User user, String rawPin, String channel){
        publisher.publishEvent(
                new ResetPasswordEvent(user, rawPin, channel)
        );
    }

    public void publisherConfirmationEvent(User user, String channel){
        publisher.publishEvent(
                new ResetPasswordConfirmation(user, channel)
        );
    }

    public void publisherConfirmationAuth(User user, String channel, String ip, String device){
        publisher.publishEvent(
                new LoginNotification(user, channel, ip, device)
        );
    }

    public void publisherRegister(User user, String channel){
        publisher.publishEvent(
                new RegisterNotification(user, channel)
        );
    }
}

