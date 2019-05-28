package cn.nextop.advance.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import cn.nextop.advance.interceptor.impl.XHandshakeInterceptor;
import cn.nextop.advance.support.websock.XWebSocketHandler;

/**
 * 
 * @author qutl
 *
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Bean
    public ServerEndpointExporter serverEndpointExporter(ApplicationContext context) {
        return new ServerEndpointExporter();
    }

    @Bean
    public XWebSocketHandler getWebSocketHandler() {
        return new XWebSocketHandler();
    }
    
    @Bean
    public XHandshakeInterceptor getHandshakeInterceptor() {
        return new XHandshakeInterceptor();
    }
    
    @Bean
    public DefaultHandshakeHandler getDefaultHandshakeHandler() {
        return new DefaultHandshakeHandler();
    }
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new XWebSocketHandler(), "/api/realtime")
        .setHandshakeHandler(getDefaultHandshakeHandler()).
        addInterceptors(getHandshakeInterceptor()).setAllowedOrigins("*");
    }
}