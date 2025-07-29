package org.template.notification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.template.common.configuration.MDCConfiguration;
import org.template.notification.service.NotificationChannel;
import org.template.notification.api.NotificationsApi;
import org.template.notification.api.dto.UniversalNotification;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class NotificationsController implements NotificationsApi {

    private final List<NotificationChannel> notificationChannels;

    @Override
    public ResponseEntity<Void> sendNotification(UniversalNotification universalNotification) {
        log.info("Creating notification by template code: %s from user: %s"
                .formatted(universalNotification.getMetaInfo().getTemplateCode(), MDC.get(MDCConfiguration.MDC_USER)));
        notificationChannels.forEach(c -> c.sendNotification(universalNotification));
        return ResponseEntity.status( HttpStatus.OK ).build();
    }
}
