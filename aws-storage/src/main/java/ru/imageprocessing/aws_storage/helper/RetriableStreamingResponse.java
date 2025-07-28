package ru.imageprocessing.aws_storage.helper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.function.Function;

@RequiredArgsConstructor
@Slf4j
public class RetriableStreamingResponse implements StreamingResponseBody {

    private static final int STREAMING_RESPONSE_BODY_BUFFER = 8192;

    private final Function<Long, InputStream> getInputStream;

    private long writtenBytes = 0;
    private int maxRetries = 5;

    @Override
    public void writeTo(OutputStream outputStream) {
        int attempt = 0;
        while (attempt <= maxRetries) {
            attempt++;
            try (InputStream inputStream = getInputStream.apply(writtenBytes)) {
                int numberOfBytesToWrite;
                byte[] data = new byte[STREAMING_RESPONSE_BODY_BUFFER];
                while ((numberOfBytesToWrite = inputStream.read(data, 0, data.length)) != -1) {
                    outputStream.write(data, 0, numberOfBytesToWrite);
                    writtenBytes += numberOfBytesToWrite;
                }
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
}
