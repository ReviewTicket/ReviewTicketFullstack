package com.reviewticket.server.notification;

import com.reviewticket.server.domain.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/subscription")
    public ResponseEntity<Void> saveSubscription(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody NotificationRequest request) {
        notificationService.saveSubscription(user.getId(), request);
        return ResponseEntity.ok().build();
    }

}
