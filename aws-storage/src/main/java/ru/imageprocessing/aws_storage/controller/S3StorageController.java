package ru.imageprocessing.aws_storage.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
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
import ru.imageprocessing.aws_storage.helper.RetriableStreamingResponse;
import ru.imageprocessing.aws_storage.mapper.S3MetadataMapper;
import ru.imageprocessing.aws_storage.messaging.FileUploadedDto;
import ru.imageprocessing.aws_storage.messaging.MessageSenderService;
import ru.imageprocessing.aws_storage.service.s3.S3Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

import static ru.imageprocessing.common.configuration.MDCConfiguration.MDC_USER;

@RequiredArgsConstructor
@Slf4j
@RestController
public class S3StorageController implements StorageApi {

    final S3Service s3Service;
    final S3MetadataMapper s3MetadataMapper;
    final MessageSenderService senderService;

    @Override
    public ResponseEntity<Void> delete(String objectKey) {
        if (s3Service.objectKeyExists(objectKey)) {
            s3Service.deleteFile(objectKey);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<StreamingResponseBody> download(String objectKey) {
        if (!s3Service.objectKeyExists(objectKey)) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();
        ResponseEntity.BodyBuilder response = ResponseEntity.ok();

        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"%s\"".formatted(FilenameUtils.getName(objectKey)));

        long contentLength = s3Service.getFileSize(objectKey);

        Function<Long, InputStream> getInputStreamRange = (Long from) -> s3Service.downloadRange(objectKey, from, contentLength);

        return response
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(contentLength)
                .body(new RetriableStreamingResponse(getInputStreamRange));
    }

    @Override
    public ResponseEntity<GetMetadata200Response> getMetadata(String objectKey) {
        return ResponseEntity.ok(s3MetadataMapper.toResponse(s3Service.getFileMetadata(objectKey)));
    }

    @Override
    public ResponseEntity<GetPresignedUrl200Response> getPresignedUrl(String objectKey, Long expiresIn) {
        GetPresignedUrl200Response response = new GetPresignedUrl200Response();
        response.setUrl(s3Service.getPresignedUrl(objectKey, expiresIn).toString());
        response.setExpiresAt(Date.from(Instant.now().plusSeconds(expiresIn)));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Upload200Response> upload(MultipartFile file, String key) {
        String objectKey = StringUtils.isBlank(key) ? file.getOriginalFilename() : key;
        try {
            s3Service.uploadStream(objectKey, file.getInputStream(), file.getSize());
            Upload200Response response = new Upload200Response();
            response.setObjectKey(objectKey);
            response.setUrl(s3Service.getUrl(objectKey).toExternalForm());
            senderService.sendFileUploaded(new FileUploadedDto(
                    MDC.get(MDC_USER),
                    s3Service.getUrl(objectKey).toExternalForm(),
                    s3Service.getPresignedUrl(objectKey, 3600L).toExternalForm()
            ));
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
