package org.template.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.template.auth.api.UsersApi;

@FeignClient(name = "usersApi", url = "${feign.auth.url}")
public interface UserRegistryClient extends UsersApi {
}
