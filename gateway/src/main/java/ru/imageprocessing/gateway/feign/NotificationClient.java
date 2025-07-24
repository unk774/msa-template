package ru.imageprocessing.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.imageprocessing.notification.api.NotificationsApi;

@FeignClient(name = "notificationApi", url = "${feign.notification.url}")
public interface NotificationClient extends NotificationsApi {
}
