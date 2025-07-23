package ru.imageprocessing.aws_storage.controller;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import ru.imageprocessing.aws_storage.api.StorageApi;
import ru.imageprocessing.aws_storage.api.dto.StorageFileIdMetadataGet200Response;
import ru.imageprocessing.aws_storage.api.dto.StorageUploadPost200Response;
import ru.imageprocessing.aws_storage.mapper.S3MetadataMapper;
import ru.imageprocessing.aws_storage.service.s3.S3RetriableDownloader;
import ru.imageprocessing.aws_storage.service.s3.S3Service;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping(path = "/api/v1/s3")
public class S3StorageController implements StorageApi {

    final S3Service s3Service;
    final S3MetadataMapper s3MetadataMapper;


    @Value("${s3.store-bucket:store}")
    private String s3StoreBucket;

    @Override
    public ResponseEntity<Void> storageFileIdDelete(String fileId) {
        if (s3Service.objectKeyExists(s3StoreBucket, fileId)) {
            s3Service.deleteFile(s3StoreBucket, fileId);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<StreamingResponseBody> storageFileIdGet(String fileId) {
        if (!s3Service.objectKeyExists(s3StoreBucket, fileId)) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        ResponseEntity.BodyBuilder response = ResponseEntity.ok();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(fileId));

        long contentLength = s3Service.getFileSize(s3StoreBucket, fileId);

        return response
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(contentLength)
                .body(new S3RetriableDownloader(s3StoreBucket, fileId, contentLength, s3Service));
    }

    @Override
    public ResponseEntity<StorageFileIdMetadataGet200Response> storageFileIdMetadataGet(String fileId) {
        return ResponseEntity.ok(s3MetadataMapper.toResponse(s3Service.getFileMetadata(s3StoreBucket, fileId)));
    }

    @Override
    public ResponseEntity<StorageUploadPost200Response> storageUploadPost(MultipartFile file) {
        String fileId = file.getOriginalFilename();
        try {
            s3Service.uploadStream(s3StoreBucket, fileId, file.getInputStream(), file.getSize());
            StorageUploadPost200Response response = new StorageUploadPost200Response();
            response.setFileId(fileId);
            response.setUrl(s3Service.getUrl(s3StoreBucket, fileId).toExternalForm());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
