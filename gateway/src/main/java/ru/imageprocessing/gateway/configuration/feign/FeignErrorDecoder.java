package ru.imageprocessing.gateway.configuration.feign;

import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.ServerErrorException;

import static feign.FeignException.errorStatus;

public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() >= 400 && response.status() <= 499) {
            return new ClientErrorException(
                    response.status()
            );
        }
        if (response.status() >= 500 && response.status() <= 599) {
            return new ServerErrorException(
                    response.status()
            );
        }
        return errorStatus(methodKey, response);
    }
}
