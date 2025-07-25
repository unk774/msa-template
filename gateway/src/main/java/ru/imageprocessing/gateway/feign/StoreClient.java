package ru.imageprocessing.gateway.feign;

import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.imageprocessing.aws_storage.api.StorageApi;

@FeignClient(name = "storeApi", url = "${feign.store.url}")
public interface StoreClient extends StorageApi {

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/api/v1/storage/download",
            produces = { "application/octet-stream", "application/json" }
    )
    Response downloadAsStream(
            @RequestParam(value = "objectKey", required = true) String objectKey
    );
}
