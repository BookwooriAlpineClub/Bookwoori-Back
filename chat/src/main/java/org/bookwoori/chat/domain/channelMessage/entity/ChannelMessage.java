package org.bookwoori.chat.domain.channelMessage.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.chat.global.common.EmojiType;
import org.bookwoori.chat.global.common.EventType;
import org.bookwoori.chat.global.common.MessageType;
import org.bookwoori.chat.global.common.dto.MemberProfileDto;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "channelMessage")
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelMessage {

    @Id
    private String id;

    private Long channelId;

    private Long memberId;

    private MessageType type;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;

    private Map<EmojiType, Set<Long>> reactions = new HashMap<>();

    private String parentId;

    /*
     * 데이터베이스에 저장되지 않는 필드
     */

    @Transient
    private String nickname;

    @Transient
    private String profileImg;

    @Transient
    private List<Long> target = new ArrayList<>();

    @Transient
    private Long parentMemberId;

    @Transient
    private String parentContent;

    @Transient
    private EventType eventType;

    @Transient
    private EmojiType targetEmoji;

    public void addReaction(EmojiType emoji, Long memberId) {
        reactions.putIfAbsent(emoji, new HashSet<>());
        reactions.get(emoji).add(memberId);
    }

    public void removeReaction(EmojiType emoji, Long memberId) {
        if (reactions.containsKey(emoji)) {
            reactions.get(emoji).remove(memberId);
            if (reactions.get(emoji).isEmpty()) {
                reactions.remove(emoji);
            }
        }
    }

    public void modifyContent(String content) {
        this.content = content;
        this.modifiedAt = LocalDateTime.now();
    }

    public void setProfile(MemberProfileDto profile) {
        this.nickname = profile.nickname();
        this.profileImg = profile.profileImg();
    }

    public void setTarget(List<Long> target) {
        this.target = target;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public void setTargetEmoji(EmojiType emoji) {
        this.targetEmoji = emoji;
    }

    public void setParentMemberId(Long parentMemberId) {
        this.parentMemberId = parentMemberId;
    }

    public void setParentContent(String parentContent) {
        this.parentContent = parentContent;
    }
}
