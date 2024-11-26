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
public class EmojiMessageRequest {

    @NotNull
    private Long userId;

    @NotNull
    private String username;

    @NotNull
    private String type;

    @NotNull
    private String content;

    @NotNull
    private Long reviewId;

    @NotNull
    private String target;

}
