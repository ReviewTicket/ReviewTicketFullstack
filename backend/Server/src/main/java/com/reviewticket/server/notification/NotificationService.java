package com.reviewticket.server.notification;

import com.reviewticket.server.domain.Notification;
import com.reviewticket.server.repository.NotificationRepository;
import com.reviewticket.server.repository.UserRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.reviewticket.server.domain.Role;

@Service
public class NotificationService {

        private final NotificationRepository notificationRepository;
        private final UserRepository userRepository;
        private final WebPushService webPushService;

        public NotificationService(
                        NotificationRepository notificationRepository,
                        UserRepository userRepository,
                        WebPushService webPushService) {
                this.notificationRepository = notificationRepository;
                this.userRepository = userRepository;
                this.webPushService = webPushService;
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

        @Scheduled(cron = "0 35 22 * * *", zone = "Asia/Seoul")
        @Transactional
        public void sendToCustomers() {

                var subscriptions = notificationRepository.findAllByUserRole(Role.CUSTOMER);

                String payload = """
                                {
                                  "title": "Review Ticket",
                                  "body": "오늘 점심은 무엇을 주문할까요?"
                                }
                                """;

                for (var subscription : subscriptions) {
                        try {
                                var response = webPushService.send(
                                                subscription.getEndpoint(),
                                                subscription.getP256dh(),
                                                subscription.getAuth(),
                                                payload);

                                int statusCode = response.getStatusLine().getStatusCode();

                                if (statusCode == 404 || statusCode == 410) {
                                        notificationRepository.deleteByEndpoint(
                                                        subscription.getEndpoint());

                                        System.out.println(
                                                        "만료된 subscription 삭제: userId="
                                                                        + subscription.getUser().getId());

                                } else {
                                        System.out.println(
                                                        "알림 발송 완료: userId="
                                                                        + subscription.getUser().getId()
                                                                        + ", status=" + statusCode);
                                }
                        } catch (Exception e) {
                                System.err.println(
                                                "알림 발송 실패: userId=" +
                                                                subscription.getUser().getId());
                                e.printStackTrace();
                        }
                }
        }
}
