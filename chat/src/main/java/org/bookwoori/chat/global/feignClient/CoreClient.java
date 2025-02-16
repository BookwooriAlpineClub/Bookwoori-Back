package org.bookwoori.chat.global.feignClient;

import java.util.Map;
import org.bookwoori.chat.global.common.dto.MemberProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "core", url = "${client.url.core}")
public interface CoreClient {

    @GetMapping("/messageRooms/{messageRoomId}/members")
    Map<Long, MemberProfileDto> getParticipantsByMessageRoom(
        @PathVariable("messageRoomId") Long messageRoomId);

    @GetMapping("/channels/{channelId}/members")
    Map<Long, MemberProfileDto> getParticipantsByChannel(
        @PathVariable("channelId") Long channelId);
}
