package org.bookwoori.core.global.feignClient;

import java.util.List;
import java.util.Map;
import org.bookwoori.core.global.feignClient.dto.RecentDirectMessageResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "core", url = "localhost:8081")
public interface ChatClient {

    @GetMapping("/messageRooms/recentMessage")
    public Map<Long, RecentDirectMessageResponseDto> getRecentMessageFromMessageRoom(
        @RequestParam("messageRoomIdList") List<Long> messageRoomIdList);
}
