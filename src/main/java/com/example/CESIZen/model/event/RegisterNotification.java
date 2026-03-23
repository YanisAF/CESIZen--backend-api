package com.example.CESIZen.model.event;

import com.example.CESIZen.model.user.User;

public record RegisterNotification(User user, String channel) {
}
