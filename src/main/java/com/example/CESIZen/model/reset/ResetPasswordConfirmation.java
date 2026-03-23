package com.example.CESIZen.model.reset;

import com.example.CESIZen.model.user.User;

public record ResetPasswordConfirmation(User user, String channel) {
}
