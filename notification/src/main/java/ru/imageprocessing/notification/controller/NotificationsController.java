package ru.imageprocessing.notification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.imageprocessing.notification.api.NotificationsApi;
import ru.imageprocessing.notification.api.dto.UniversalNotification;
import ru.imageprocessing.notification.configuration.MDCConfiguration;
import ru.imageprocessing.notification.service.NotificationsService;

@RestController
@Slf4j
@RequiredArgsConstructor
public class NotificationsController implements NotificationsApi {

    private final NotificationsService notificationsService;

    @Override
    public ResponseEntity<Void> sendNotification(UniversalNotification universalNotification) {
        log.info("Creating notification by template code: %s from user: %s"
                .formatted(universalNotification.getMetaInfo().getTemplateCode(), MDC.get(MDCConfiguration.MDC_USER)));
        notificationsService.createNotification(universalNotification);
        return ResponseEntity.status( HttpStatus.OK ).build();
    }
}
