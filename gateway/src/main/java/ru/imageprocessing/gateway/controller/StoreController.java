package ru.imageprocessing.gateway.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.imageprocessing.aws_storage.api.StorageApi;
import ru.imageprocessing.aws_storage.api.dto.GetMetadata200Response;
import ru.imageprocessing.aws_storage.api.dto.GetPresignedUrl200Response;
import ru.imageprocessing.aws_storage.api.dto.Upload200Response;
import ru.imageprocessing.gateway.feign.StoreClient;

import java.io.IOException;

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
    public ResponseEntity<StreamingResponseBody> download(String objectKey) {
        var response = storeClient.downloadAsStream(objectKey);
        if (response.status() != 200) {
            return ResponseEntity.status(response.status()).build();
        }

        var headers = new HttpHeaders();
        var okResponse = ResponseEntity.ok();

        headers.set(HttpHeaders.CONTENT_DISPOSITION, response.headers().get(HttpHeaders.CONTENT_DISPOSITION).stream().findFirst().orElse(null));
        headers.set(HttpHeaders.CONTENT_LENGTH, response.headers().get(HttpHeaders.CONTENT_LENGTH).stream().findFirst().orElse(null));

        return okResponse
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body((outputStream) -> {
            try (var is = response.body().asInputStream()){
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException ioException) {
                log.error("error reading download stream", ioException);
            }
        });
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
