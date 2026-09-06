package com.reviewticket.server.repository;

import com.reviewticket.server.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    Optional<NotificationRepository> findByEndpoint(String endpoint);
}
