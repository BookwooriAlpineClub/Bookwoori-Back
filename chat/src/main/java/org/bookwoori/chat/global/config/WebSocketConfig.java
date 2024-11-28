package org.bookwoori.chat.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final CustomChannelInterceptor customChannelInterceptor;

    /*
     * 클라이언트가 웹소켓 서버에 연결할 수 있는 엔드포인트를 등록
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket-endpoint").setAllowedOriginPatterns("*");
        registry.addEndpoint("/websocket-endpoint").setAllowedOriginPatterns("*").withSockJS();
    }

    /*
     * 웹소켓 통신의 성능과 관련된 설정
     * 메시지의 최대 크기, 메시지 전송 시간, 전송 버퍼 사이즈 등에 대한 설정 가능
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        WebSocketMessageBrokerConfigurer.super.configureWebSocketTransport(registry);
    }

    /*
     * 클라이언트에서 서버로 들어오는 WebSocket 메시지를 처리하는 인바운드 설정
     * 인터셉터를 등록하면 메시지에 대한 사전 처리나 필터링 등의 작업 수행 가능
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(customChannelInterceptor);
    }

    /*
     * 메시지 브로커 구성
     * setApplicationDestinationPrefixes : 메시지 발행 경로의 prefix 설정 → 서버는 "/pub" prefix를 통해 컨트롤러의 @MessageMapping 메소드로 메시지를 전달
     * enableSimpleBroker : 구독 경로의 prefix 설정
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub");
        registry.enableSimpleBroker("/topic");
    }
}
