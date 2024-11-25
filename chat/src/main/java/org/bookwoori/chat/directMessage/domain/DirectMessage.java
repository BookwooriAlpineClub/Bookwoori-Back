package org.bookwoori.chat.directMessage.domain;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.chat.global.common.EmojiType;
import org.bookwoori.chat.global.common.MessageType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "directMessage")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DirectMessage {

    @Id
    private String id;

    private Long messageRoomId;

    private Long memberId;

    private MessageType type;

    private String content;

    private LocalDateTime createdAt;

    private Map<EmojiType, Set<Long>> reactions = new HashMap<>();

    private String parentId;

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
}
