package ru.imageprocessing.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.imageprocessing.aws_storage.api.StorageApi;
import ru.imageprocessing.aws_storage.api.dto.GetMetadata200Response;
import ru.imageprocessing.aws_storage.api.dto.Upload200Response;
import ru.imageprocessing.gateway.feign.StoreClient;

@RequiredArgsConstructor
@Slf4j
@RestController
public class StoreController implements StorageApi {

    private final StoreClient storeClient;

    @Override
    public ResponseEntity<Void> delete(String fileId) {
        return storeClient.delete(fileId);
    }

    @Override
    public ResponseEntity<StreamingResponseBody> download(String fileId) {
        return storeClient.download(fileId);
    }

    @Override
    public ResponseEntity<GetMetadata200Response> getMetadata(String fileId) {
        return storeClient.getMetadata(fileId);
    }

    @Override
    public ResponseEntity<Upload200Response> upload(MultipartFile file) {
        return storeClient.upload(file);
    }
}
