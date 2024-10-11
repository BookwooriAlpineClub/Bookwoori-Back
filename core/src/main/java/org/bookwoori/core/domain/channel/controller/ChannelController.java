package org.bookwoori.core.domain.channel.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Channel")
@RequiredArgsConstructor
@RequestMapping("/channels")
public class ChannelController {

}