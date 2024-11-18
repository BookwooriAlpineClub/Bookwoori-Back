package org.bookwoori.chat.directMessage.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    private String type;

    private String content;

    @Override
    public String toString() {
        return "DirectMessage{" +
            "id='" + id + '\'' +
            ", messageRoomId=" + messageRoomId +
            ", memberId=" + memberId +
            ", contentType='" + type + '\'' +
            ", content='" + content + '\'' +
            '}';
    }
}
