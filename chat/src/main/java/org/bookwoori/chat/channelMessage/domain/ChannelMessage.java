package org.bookwoori.chat.channelMessage.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bookwoori.chat.global.MessageType;
import org.springframework.data.annotation.Id;
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

    @Override
    public String toString() {
        return "ChannelMessage{" +
            "id='" + id + '\'' +
            ", channelId=" + channelId +
            ", memberId=" + memberId +
            ", type='" + type + '\'' +
            ", content='" + content + '\'' +
            ", createdAt=" + createdAt +
            '}';
    }
}
