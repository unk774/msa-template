package ru.imageprocessing.common.configuration.feign;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;

public class StreamingResponseBodyHttpMessageConverter extends AbstractHttpMessageConverter<StreamingResponseBody> {

    public StreamingResponseBodyHttpMessageConverter() {
        super(MediaType.ALL);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return StreamingResponseBody.class.isAssignableFrom(clazz);
    }

    @Override
    protected StreamingResponseBody readInternal(
            Class<? extends StreamingResponseBody> clazz,
            HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        return outputStream -> {
            try (InputStream inputStream = inputMessage.getBody()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        };
    }

    @Override
    protected void writeInternal(
            StreamingResponseBody streamingResponseBody,
            HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        streamingResponseBody.writeTo(outputMessage.getBody());
    }
}
