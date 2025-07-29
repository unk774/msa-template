package org.template.common.configuration.feign;

import feign.Response;
import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

@Slf4j
public class AutoClosingResponseEntityDecoder extends ResponseEntityDecoder {

    public AutoClosingResponseEntityDecoder(Decoder decoder) {
        super(decoder);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException {
        Object result = super.decode(response, type);
        if (shouldClose(type)) {
            feign.Util.ensureClosed(response);
        }
        return result;
    }

    private boolean shouldClose(Type type) {
        try {
            var typeClass = (Class<?>) Arrays.stream(((ParameterizedType) type).getActualTypeArguments()).findFirst().orElse(null);
            return !StreamingResponseBody.class.isAssignableFrom(typeClass);
        } catch (Exception e) {
            log.error("error while checking response type", e);
            return false;
        }
    }
}