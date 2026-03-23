package com.example.CESIZen.model.reset;

import com.example.CESIZen.model.user.User;

public record ResetPasswordEvent(User user, String rawPin, String channel) {
}
