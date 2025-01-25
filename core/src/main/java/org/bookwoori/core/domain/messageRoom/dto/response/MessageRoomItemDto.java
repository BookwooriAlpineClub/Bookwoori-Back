package org.bookwoori.core.domain.messageRoom.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.bookwoori.core.domain.member.infrastructure.MemberEntity;
import org.bookwoori.core.global.feignClient.dto.RecentDirectMessageResponseDto;

@Builder
public record MessageRoomItemDto(
    Long messageRoomId,
    Long memberId,
    String nickname,
    String profileImg,
    String recentMessage, //가장 최근에 도착한 문자 메시지
    LocalDateTime recentMessageTime //recentMessage 가 도착한 시간
) {

    public static MessageRoomItemDto from(Long messageRoomId, MemberEntity partner,
        RecentDirectMessageResponseDto recentMessage) {
        return MessageRoomItemDto.builder()
            .messageRoomId(messageRoomId)
            .memberId(partner.getMemberId())
            .nickname(partner.getNickname())
            .profileImg(partner.getProfileImg())
            .recentMessage(recentMessage.content())
            .recentMessageTime(recentMessage.sendAt())
            .build();
    }
}
