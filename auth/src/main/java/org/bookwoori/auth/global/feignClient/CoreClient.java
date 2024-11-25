package org.bookwoori.auth.global.feignClient;

import org.bookwoori.auth.global.feignClient.dto.GetMemberResponseDto;
import org.bookwoori.auth.global.feignClient.dto.GetOrSaveMemberRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "core", url = "${client.url.core}")
public interface CoreClient {

    @PostMapping("/auth/members")
    ResponseEntity<GetMemberResponseDto> getOrSaveMember(GetOrSaveMemberRequestDto requestDto);

}
