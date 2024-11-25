package org.bookwoori.core.domain.messageRoom.dto.request;

import jakarta.validation.constraints.NotNull;
import org.bookwoori.core.domain.member.entity.Member;
import org.bookwoori.core.domain.messageRoom.entity.MessageRoom;

public record MessageRoomCreateRequestDto(
    @NotNull
    Long memberId
) {

    public MessageRoom toEntity(Member sender, Member receiver) {
        return MessageRoom.builder()
            .sender(sender)
            .receiver(receiver)
            .build();
    }
}
