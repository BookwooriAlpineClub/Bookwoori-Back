package org.bookwoori.chat.global.feignClient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "core", url = "${client.url.core}")
public interface CoreClient {

}
