package org.bookwoori.notification.domain;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Document(collation = "notification")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Notification {

    @Id
    @Field("id")
    private String id;
    private String senderId;
    private String senderName;
    private String type;
    private String content;
    private String channelId;
    private String channelName;
    private String target;

    // TODO id는 ID 생성기로 만든 값으로 하자
    public Notification(
            String senderId,
            String senderName,
            String type,
            String content,
            String channelId,
            String channelName,
            String target
    ) {
        this.id = UUID.randomUUID().toString();
        this.senderId = senderId;
        this.senderName = senderName;
        this.type = type;
        this.content = content;
        this.channelId = channelId;
        this.channelName = channelName;
        this.target = target;
    }
}
