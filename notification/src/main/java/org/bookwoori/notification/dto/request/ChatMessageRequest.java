package org.bookwoori.notification.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRequest {

    @NotNull
    private Long userId;

    @NotNull
    private String username;

    @NotNull
    private String type;

    @NotNull
    private String content;

    @NotNull
    private String channelName;

    @NotNull
    private Long communityId;

    @NotNull
    private Long channelId;

    @NotNull
    private String target;
}

