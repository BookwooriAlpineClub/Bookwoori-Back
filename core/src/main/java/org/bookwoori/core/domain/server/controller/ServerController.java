package org.bookwoori.core.domain.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookwoori.core.domain.server.dto.request.ServerCreateRequestDto;
import org.bookwoori.core.domain.server.facade.ServerFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Server")
@RequiredArgsConstructor
@RequestMapping("/servers")
public class ServerController {

    private final ServerFacade serverFacade;

    @Operation(summary = "모임 서버 생성", description = "모임 서버를 생성합니다.")
    @PostMapping
    public ResponseEntity<?> createServer(@RequestBody @Valid ServerCreateRequestDto requestDto) {
        serverFacade.createServer(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
