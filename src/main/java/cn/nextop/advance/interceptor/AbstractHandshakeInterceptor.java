package cn.nextop.advance.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

/**
 * 
 * @author qutl
 *
 */
public abstract class AbstractHandshakeInterceptor implements HandshakeInterceptor  {

	/**
	 * 
	 */
	protected final HttpSession getSession(final ServerHttpRequest request) {
		HttpServletRequest servlet = getServletRequest(request); return (servlet == null) ? null : servlet.getSession(false);
	}
	
	protected final HttpServletRequest getServletRequest(ServerHttpRequest request) {
		return request instanceof ServletServerHttpRequest ? ((ServletServerHttpRequest) request).getServletRequest() : null;
	}
	
	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
	}
	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) 
			throws Exception {
		return true;
	}
}