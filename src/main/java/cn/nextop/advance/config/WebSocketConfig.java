package cn.nextop.advance.config;

import static org.eclipse.jetty.websocket.api.WebSocketBehavior.SERVER;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.google.common.collect.Lists;

import cn.nextop.advance.interceptor.impl.XHandshakeInterceptor;
import cn.nextop.advance.realtime.XWebSocketChannel;
import cn.nextop.advance.realtime.channel.api.customer.CustomerChannel;
import cn.nextop.advance.support.realtime.XRequestUpgradeStrategy;
import cn.nextop.advance.support.realtime.XWebSocketHandler;
import cn.nextop.advance.support.realtime.XWebSocketPolicy;

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
    	XWebSocketHandler handler = new XWebSocketHandler();
    	List<XWebSocketChannel> channels = Lists.newArrayList();
    	channels.add(new CustomerChannel()); handler.setChannels(channels);
        return handler;
    }
    
    @Bean
    public XHandshakeInterceptor getHandshakeInterceptor() {
        return new XHandshakeInterceptor();
    }
    
    @Bean
    public DefaultHandshakeHandler getDefaultHandshakeHandler() {
    	XWebSocketPolicy policy = new XWebSocketPolicy(SERVER);
    	XRequestUpgradeStrategy rus = new XRequestUpgradeStrategy(policy);
        return new DefaultHandshakeHandler(rus);
    }
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getWebSocketHandler(), "/api/realtime")
        .setHandshakeHandler(getDefaultHandshakeHandler()).
        addInterceptors(getHandshakeInterceptor()).setAllowedOrigins("*");
    }
}