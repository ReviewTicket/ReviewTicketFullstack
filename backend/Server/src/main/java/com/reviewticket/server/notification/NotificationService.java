package com.reviewticket.server.notification;

import com.reviewticket.server.domain.Notification;
import com.reviewticket.server.repository.NotificationRepository;
import com.reviewticket.server.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(
            NotificationRepository notificationRepository,
            UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveSubscription(
            Long userId,
            NotificationRequest request) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        var existing = notificationRepository
                .findByEndpoint(request.endpoint());

        if (existing.isPresent()) {
            return;
        }

        notificationRepository.save(
                new Notification(
                        user,
                        request.endpoint(),
                        request.p256dh(),
                        request.auth()));
    }
}
