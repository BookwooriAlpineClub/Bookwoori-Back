package org.bookwoori.core.domain.messageRoom.dto.request;

import jakarta.validation.constraints.NotNull;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.domain.messageRoom.infrastructure.MessageRoomEntity;

public record MessageRoomCreateRequestDto(
    @NotNull
    Long memberId
) {

    public MessageRoomEntity toEntity(MemberEntity sender, MemberEntity receiver) {
        return MessageRoomEntity.builder()
            .sender(sender)
            .receiver(receiver)
            .build();
    }
}
