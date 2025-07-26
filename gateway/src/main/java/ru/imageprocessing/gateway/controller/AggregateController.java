package ru.imageprocessing.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.imageprocessing.auth.api.dto.UserResponse;
import ru.imageprocessing.gateway.api.AggregateApi;
import ru.imageprocessing.gateway.api.dto.UploadAndNotify200Response;
import ru.imageprocessing.gateway.feign.NotificationClient;
import ru.imageprocessing.gateway.feign.StoreClient;
import ru.imageprocessing.gateway.feign.UserRegistryClient;
import ru.imageprocessing.notification.api.dto.MetaInfo;
import ru.imageprocessing.notification.api.dto.UniversalNotification;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import static ru.imageprocessing.common.configuration.MDCConfiguration.MDC_USER;


@RequiredArgsConstructor
@Slf4j
@RestController
public class AggregateController implements AggregateApi {

    private final StoreClient storeClient;

    @Override
    public ResponseEntity<UploadAndNotify200Response> uploadAndNotify(MultipartFile file) {
        if (StringUtils.isBlank(MDC.get(MDC_USER))) {
            throw new RuntimeException("user not found in MDC");
        }

        String objectKey = MDC.get(MDC_USER) + "/" + file.getOriginalFilename();
        storeClient.upload(file, objectKey).getBody();

        String presignedUrl = storeClient.getPresignedUrl(objectKey, 900L).getBody().getUrl();
        var response = new UploadAndNotify200Response();
        response.setUrl(presignedUrl);
        response.setObjectKey(objectKey);

        return ResponseEntity.ok(response);
    }
}
