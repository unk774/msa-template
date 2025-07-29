package org.template.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.template.gateway.feign.StoreClient;
import org.template.auth.api.dto.UserResponse;
import org.template.gateway.api.AggregateApi;
import org.template.gateway.api.dto.UploadAndNotify200Response;
import org.template.notification.api.dto.MetaInfo;
import org.template.notification.api.dto.UniversalNotification;

import static org.template.common.configuration.MDCConfiguration.MDC_USER;


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
