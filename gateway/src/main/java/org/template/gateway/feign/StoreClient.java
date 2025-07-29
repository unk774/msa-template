package org.template.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.template.aws_storage.api.StorageApi;

@FeignClient(name = "storeApi", url = "${feign.store.url}")
public interface StoreClient extends StorageApi {
}
