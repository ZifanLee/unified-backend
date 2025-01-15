package com.zifan.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketConfig.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 启用 RabbitMQ 作为外部消息代理
        config.enableStompBrokerRelay("/topic", "/queue")
                .setRelayHost("localhost")
                .setRelayPort(61613)
                .setVirtualHost("/")
                .setClientLogin("tiger")
                .setClientPasscode("tiger")
                .setSystemLogin("admin")
                .setSystemPasscode("admin123");

        // 客户端发送消息的前缀
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册 STOMP 端点，客户端通过这个端点连接 WebSocket
        registry.addEndpoint("/ws");
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // ChannelInterceptor，用于记录每个请求的路由信息
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                // 获取 STOMP 头信息
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null) {
                    // 记录 STOMP 命令和路由信息
                    StompCommand command = accessor.getCommand();
                    String destination = accessor.getDestination();
                    String sessionId = accessor.getSessionId();

                    logger.info("Received STOMP command: {}", command);
                    logger.info("Destination: {}", destination);
                    logger.info("Session ID: {}", sessionId);

                    // 记录所有头信息
                    accessor.toMap().forEach((key, value) -> {
                        logger.info("Header - {}: {}", key, value);
                    });
                }

                return message;
            }
        });
    }
}