package cn.nextop.advance.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
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
}
