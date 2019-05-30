package cn.nextop.advance.interceptor.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.nextop.advance.interceptor.AbstractHandlerInterceptor;

/**
 * 
 * @author qutl
 *
 */
public class HttpAuthorizationInterceptor extends AbstractHandlerInterceptor {
	
	/**
	 * 
	 */
	public HttpAuthorizationInterceptor() {
		super("web.advance.interceptor.authorization");
	}
	
	/**
	 * 
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		return true;
	}
}
