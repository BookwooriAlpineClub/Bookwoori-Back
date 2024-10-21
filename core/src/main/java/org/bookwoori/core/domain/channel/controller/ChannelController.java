package org.bookwoori.core.domain.channel.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.channel.dto.request.ChannelCreateRequestDto;
import org.bookwoori.core.domain.channel.dto.request.ChannelUpdateRequestDto;
import org.bookwoori.core.domain.channel.facade.ChannelFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Channel")
@RequiredArgsConstructor
@RequestMapping("/channels")
public class ChannelController {

    private final ChannelFacade channelFacade;

    @Operation(summary = "채널 생성", description = "채팅/음성 채널을 생성합니다.")
    @PostMapping
    public ResponseEntity<?> createChannel(@Valid @RequestBody ChannelCreateRequestDto requestDto) {
        channelFacade.createChannel(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "채널 이름 변경", description = "채팅/음성 채널의 이름을 변경합니다.")
    @PatchMapping("/{channelId}/name")
    public ResponseEntity<?> updateChannelName(@PathVariable final Long channelId,
        @Valid @RequestBody ChannelUpdateRequestDto requestDto) {
        channelFacade.updateChannelName(channelId, requestDto);
        return ResponseEntity.ok().build();
    }
}
