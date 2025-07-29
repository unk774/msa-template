package org.template.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.template.auth.api.AuthApi;

@FeignClient(name = "authApi", url = "${feign.auth.url}")
public interface AuthClient extends AuthApi {
}
