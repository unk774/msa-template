package ru.imageprocessing.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.imageprocessing.auth.api.AuthApi;

@FeignClient(name = "authApi", url = "${feign.auth.url}")
public interface AuthClient extends AuthApi {
}
