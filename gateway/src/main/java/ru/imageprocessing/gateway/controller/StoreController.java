package ru.imageprocessing.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.imageprocessing.aws_storage.api.StorageApi;
import ru.imageprocessing.aws_storage.api.dto.ApiV1StorageFileIdMetadataGet200Response;
import ru.imageprocessing.aws_storage.api.dto.ApiV1StorageUploadPost200Response;
import ru.imageprocessing.gateway.feign.StoreClient;

@RequiredArgsConstructor
@Slf4j
@RestController
public class StoreController implements StorageApi {

    private final StoreClient storeClient;

    @Override
    public ResponseEntity<Void> apiV1StorageFileIdDelete(String fileId) {
        return storeClient.apiV1StorageFileIdDelete(fileId);
    }

    @Override
    public ResponseEntity<StreamingResponseBody> apiV1StorageFileIdGet(String fileId) {
        return storeClient.apiV1StorageFileIdGet(fileId);
    }

    @Override
    public ResponseEntity<ApiV1StorageFileIdMetadataGet200Response> apiV1StorageFileIdMetadataGet(String fileId) {
        return storeClient.apiV1StorageFileIdMetadataGet(fileId);
    }

    @Override
    public ResponseEntity<ApiV1StorageUploadPost200Response> apiV1StorageUploadPost(MultipartFile file) {
        return storeClient.apiV1StorageUploadPost(file);
    }
}
