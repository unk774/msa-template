package ru.imageprocessing.gateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import ru.imageprocessing.aws_storage.api.StorageApi;

@FeignClient(name = "storeApi", url = "${feign.store.url}")
public interface StoreClient extends StorageApi {
}
