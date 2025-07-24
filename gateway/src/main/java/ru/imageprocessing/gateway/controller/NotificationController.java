package ru.imageprocessing.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.imageprocessing.gateway.feign.NotificationClient;
import ru.imageprocessing.notification.api.NotificationsApi;
import ru.imageprocessing.notification.api.dto.UniversalNotification;

@RequiredArgsConstructor
@Slf4j
@RestController
public class NotificationController implements NotificationsApi {

    private final NotificationClient notificationClient;

    @Override
    public ResponseEntity<Void> sendNotification(UniversalNotification universalNotification) {
        return notificationClient.sendNotification(universalNotification);
    }
}
