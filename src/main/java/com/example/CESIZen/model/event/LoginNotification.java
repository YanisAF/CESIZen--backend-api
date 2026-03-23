package com.example.CESIZen.model.event;

import com.example.CESIZen.model.user.User;

public record LoginNotification(User user, String channel, String ip, String device) {
}
