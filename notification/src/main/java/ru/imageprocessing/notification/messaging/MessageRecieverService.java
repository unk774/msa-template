package ru.imageprocessing.notification.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import ru.imageprocessing.auth.api.dto.UserResponse;
import ru.imageprocessing.aws_storage.messaging.FileUploadedDto;
import ru.imageprocessing.notification.api.dto.MetaInfo;
import ru.imageprocessing.notification.api.dto.UniversalNotification;
import ru.imageprocessing.notification.feign.UserRegistryClient;
import ru.imageprocessing.notification.service.NotificationsService;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Consumer;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageRecieverService {

    private final NotificationsService notificationsService;
    private final UserRegistryClient userRegistryClient;

    @Bean
    public Consumer<Message<FileUploadedDto>> upload() {
        return message -> {
            FileUploadedDto fileUploadedDto = message.getPayload();

            UserResponse user = userRegistryClient.getUserByLogin(fileUploadedDto.getLogin()).getBody();
            //reply if email empty
            if (!StringUtils.isBlank(user.getEmail())) {
                //send notification
                MetaInfo metaInfo = new MetaInfo();
                metaInfo.setNotificationChannel(MetaInfo.NotificationChannelEnum.EMAIL);
                metaInfo.setSender("noreply@noreply");
                metaInfo.setRecipient(user.getEmail());
                metaInfo.setDate(Date.from(Instant.now()));
                metaInfo.setSubject("Uploaded file link");
                metaInfo.setTemplateFormat(MetaInfo.TemplateFormatEnum.HTML);
                metaInfo.setTemplateCode("notification-template.html");

                UniversalNotification universalNotification = new UniversalNotification();
                universalNotification.setMetaInfo(metaInfo);

                universalNotification.setData(Map.of("firstName", StringUtils.defaultIfBlank(user.getFirstName(),""),
                        "lastName", StringUtils.defaultIfBlank(user.getLastName(),""),
                        "downloadLink", fileUploadedDto.getPresignedUrl()));

                notificationsService.createNotification(universalNotification);
            }
        };
    }
}
