package cn.nextop.advance.interceptor.impl;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;

import cn.nextop.advance.interceptor.AbstractHandlerInterceptor;
import cn.nextop.advance.support.security.SecurityContexts;
import cn.nextop.advance.support.util.HttpRequests;

/**
 * 
 * @author qutl
 *
 */
public class HttpAuthenticationInterceptor extends AbstractHandlerInterceptor {
	//
	private final static Logger ATTACK = LoggerFactory.getLogger("ATTACK_LOGGER");
	
	/**
	 * 
	 */
	public HttpAuthenticationInterceptor() {
		super("web.advance.interceptor.authentication");
	}
	
	/**
	 * Setup SecurityContext for Jetty worker's fast-thread-local
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	throws Exception { System.out.println(1);
		// 1
		if ((!(handler instanceof HandlerMethod))) return true;
		final HttpSession v = request.getSession(false /* not create */ );
		SecurityContexts.Context context = new SecurityContexts.Context();
		context.setSessionId(v == null ? null : v.getId()); 
		
		// validate
		final String ip = trimToEmpty(HttpRequests.getIp(request));
		ATTACK.info ("ACCESS {},{}", ip, HttpRequests.toString(request));
		return true;
	}
}
