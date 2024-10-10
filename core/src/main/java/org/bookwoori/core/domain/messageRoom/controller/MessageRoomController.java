package org.bookwoori.core.domain.messageRoom.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "MessageRoom")
@RequiredArgsConstructor
@RequestMapping("/message-rooms")
public class MessageRoomController {

}
