package com.reviewticket.server.repository;

import com.reviewticket.server.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.reviewticket.server.domain.Role;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    Optional<NotificationRepository> findByEndpoint(String endpoint);

    @Query("""
                SELECT n
                FROM Notification n
                JOIN FETCH n.user u
                WHERE u.role = :role
            """)
    List<Notification> findAllByUserRole(@Param("role") Role role);
}
