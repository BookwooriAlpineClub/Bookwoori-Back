package org.bookwoori.chat.global.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.bookwoori.chat.global.jwt.TokenProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.server.ResponseStatusException;

@Configuration
@RequiredArgsConstructor
@Log4j2
public class CustomChannelInterceptor implements ChannelInterceptor {

    private final TokenProvider tokenProvider;

    /*
     * 메시지가 실제로 채널로 보내지기 전에 호출되는 메소드
     * 웹소켓 최초 연결 시도 시 토큰 인증 수행
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        var accessor = StompHeaderAccessor.wrap(message);
        if (accessor.getCommand() == StompCommand.CONNECT) {
            String token = accessor.getFirstNativeHeader("Authorization");
            if (!tokenProvider.validateToken(token)) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }
        return message;
    }
}
