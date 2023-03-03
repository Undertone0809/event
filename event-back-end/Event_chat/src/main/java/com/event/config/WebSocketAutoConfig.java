package com.event.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * websocket配置
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketAutoConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")         //开启/bullet端点
                .setAllowedOrigins("*")         //允许跨域访问
                .withSockJS();                  //使用sockJS
    }
}