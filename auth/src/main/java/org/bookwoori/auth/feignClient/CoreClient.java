package org.bookwoori.auth.feignClient;

import org.bookwoori.auth.dto.GetMemberResponseDto;
import org.bookwoori.auth.dto.GetOrSaveMemberRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "core", url = "${client.url.core}")
public interface CoreClient {

    @PostMapping("/auth/members")
    ResponseEntity<GetMemberResponseDto> getOrSaveMember(GetOrSaveMemberRequestDto requestDto);
}
