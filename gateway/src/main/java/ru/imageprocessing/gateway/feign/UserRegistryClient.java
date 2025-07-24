package ru.imageprocessing.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.imageprocessing.auth.api.UsersApi;

@FeignClient(name = "usersApi", url = "${feign.auth.url}")
public interface UserRegistryClient extends UsersApi {
}
