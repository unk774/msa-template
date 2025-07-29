package ru.imageprocessing.notification.service;

import ru.imageprocessing.notification.api.dto.UniversalNotification;

public interface NotificationChannel {

    public void sendNotification(UniversalNotification universalNotification);
}
