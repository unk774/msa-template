package ru.imageprocessing.aws_storage.service.s3;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RequiredArgsConstructor
@Slf4j
public class S3RetriableDownloader implements StreamingResponseBody, S3Downloader {

    private static final int STREAMING_RESPONSE_BODY_BUFFER = 8192;

    private final String bucketName;
    private final String objectKey;
    private final Long contentLength;
    private final S3Service s3Service;

    private long writtenBytes = 0;
    private int maxRetries = 5;
    private OutputStream outputStream;

    @Override
    public void writeTo(OutputStream outputStream) {
        this.outputStream = outputStream;
        int attempt = 0;
        while (attempt <= maxRetries) {
            attempt++;
            try {
                s3Service.downloadFileRange(bucketName, objectKey, writtenBytes, contentLength, this);
                log.info("written bytes %s".formatted(writtenBytes));
                return;
            } catch (Exception e) {
                log.error("error while downloading. attempt %s".formatted(attempt), e);
                if (attempt >= maxRetries) {
                    throw new RuntimeException("download failed after " + maxRetries + " attempts", e);
                }
            }
        }
    }

    @Override
    public void download(InputStream inputStream) {
        try {
            int numberOfBytesToWrite;
            byte[] data = new byte[STREAMING_RESPONSE_BODY_BUFFER];
            while ((numberOfBytesToWrite = inputStream.read(data, 0, data.length)) != -1) {
                outputStream.write(data, 0, numberOfBytesToWrite);
                writtenBytes += numberOfBytesToWrite;
            }
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }
}
