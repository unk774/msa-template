package ru.imageprocessing.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.imageprocessing.aws_storage.api.StorageApi;
import ru.imageprocessing.aws_storage.api.dto.GetMetadata200Response;
import ru.imageprocessing.aws_storage.api.dto.GetPresignedUrl200Response;
import ru.imageprocessing.aws_storage.api.dto.Upload200Response;
import ru.imageprocessing.gateway.feign.StoreClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Slf4j
@RestController
public class StoreController implements StorageApi {

    private final StoreClient storeClient;

    @Override
    //TODO not found handling
    public ResponseEntity<Void> delete(String objectKey) {
        return storeClient.delete(objectKey);
    }

    @Override
    //TODO feign cant handle StreamingResponseBody
    public ResponseEntity<StreamingResponseBody> download(String objectKey) {
        return storeClient.download(objectKey);
    }

    @Override
    public ResponseEntity<GetMetadata200Response> getMetadata(String objectKey) {
        return storeClient.getMetadata(objectKey);
    }

    @Override
    public ResponseEntity<GetPresignedUrl200Response> getPresignedUrl(String objectKey, Long expiresIn) {
        return storeClient.getPresignedUrl(objectKey, expiresIn);
    }

    @Override
    public ResponseEntity<Upload200Response> upload(MultipartFile file, String objectKey) {
        return storeClient.upload(file, objectKey);
    }
}
