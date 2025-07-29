package org.template.notification.service;

import org.template.notification.api.dto.UniversalNotification;

public interface NotificationChannel {

    public void sendNotification(UniversalNotification universalNotification);
}
