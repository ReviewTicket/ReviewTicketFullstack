package com.reviewticket.server.notification;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Subscription;
import java.security.GeneralSecurityException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Security;

@Service
public class WebPushService {

    private final PushService pushService;

    public WebPushService(
            @Value("${webpush.vapid.public-key}") String publicKey,
            @Value("${webpush.vapid.private-key}") String privateKey,
            @Value("${webpush.vapid.subject}") String subject) {
        try {
            Security.addProvider(new BouncyCastleProvider());

            this.pushService = new PushService(publicKey, privateKey, subject);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "VAPID 키 초기화에 실패했습니다.",
                    e);
        }
    }

    public void send(
            String endpoint,
            String p256dh,
            String auth,
            String payload) throws Exception {

        Subscription.Keys keys = new Subscription.Keys(p256dh, auth);

        Subscription subscription = new Subscription(endpoint, keys);

        Notification notification = new Notification(subscription, payload);

        pushService.send(
                notification);
    }
}
