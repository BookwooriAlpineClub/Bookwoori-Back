package org.bookwoori.core.global.feignClient.dto;

import java.time.LocalDateTime;

public record RecentDirectMessageResponseDto(
    String content,
    LocalDateTime sendAt) {

}
