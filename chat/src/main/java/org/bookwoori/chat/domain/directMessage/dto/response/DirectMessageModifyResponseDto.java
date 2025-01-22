package org.bookwoori.chat.domain.directMessage.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.chat.domain.directMessage.entity.DirectMessage;

@Builder
public record DirectMessageModifyResponseDto(
    String id,
    String content,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime modifiedAt
) {

    public static DirectMessageModifyResponseDto from(DirectMessage directMessage) {
        return DirectMessageModifyResponseDto.builder()
            .id(directMessage.getId())
            .content(directMessage.getContent())
            .modifiedAt(directMessage.getModifiedAt())
            .build();
    }

}
