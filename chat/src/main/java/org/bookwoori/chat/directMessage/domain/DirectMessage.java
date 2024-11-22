package org.bookwoori.chat.directMessage.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.chat.global.MessageType;
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

    @Override
    public String toString() {
        return "DirectMessage{" +
            "id='" + id + '\'' +
            ", messageRoomId=" + messageRoomId +
            ", memberId=" + memberId +
            ", type=" + type +
            ", content='" + content + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }
}
