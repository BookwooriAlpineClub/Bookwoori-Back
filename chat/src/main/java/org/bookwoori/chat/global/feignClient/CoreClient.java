package org.bookwoori.chat.global.feignClient;

import java.util.Map;
import org.bookwoori.chat.global.feignClient.dto.MemberProfileResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "core", url = "${client.url.core}")
public interface CoreClient {

    @GetMapping("/messageRooms/{messageRoomId}/members")
    public Map<Long, MemberProfileResponseDto> getMembersByMessageRoomId(
        @PathVariable final Long messageRoomId);
}
