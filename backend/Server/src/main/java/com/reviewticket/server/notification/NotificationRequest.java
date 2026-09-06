package com.reviewticket.server.notification;

import jakarta.validation.constraints.NotBlank;

public record NotificationRequest(
                @NotBlank String endpoint,
                @NotBlank String p256dh,
                @NotBlank String auth) {
}
