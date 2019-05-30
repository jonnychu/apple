package cn.nextop.advance.interceptor.impl;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;

import cn.nextop.advance.interceptor.AbstractHandshakeInterceptor;
import cn.nextop.advance.support.util.HttpRequests;

/**
 * 
 * @author qutl
 *
 */
public class XHandshakeInterceptor extends AbstractHandshakeInterceptor {

	/**
	 * 
	 */
	public XHandshakeInterceptor() {
		super("web.advance.interceptor.handshake");
	}
	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {
		//
		final String token = HttpRequests.getAccessToken(getServletRequest(request));
		//
		String sessionId = token; // convert sessionId
		//
		attributes.put(HttpRequests.SESSION_ID_KEY, sessionId); return (true);
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
	}
}