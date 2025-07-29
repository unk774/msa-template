package org.template.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.template.notification.api.NotificationsApi;

@FeignClient(name = "notificationApi", url = "${feign.notification.url}")
public interface NotificationClient extends NotificationsApi {
}
