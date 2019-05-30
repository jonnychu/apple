package cn.nextop.advance.interceptor.impl;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;

import cn.nextop.advance.interceptor.AbstractHandlerInterceptor;
import cn.nextop.advance.support.util.HttpRequests;
import nl.bitwalker.useragentutils.UserAgent;

/**
 * 
 * @author qutl
 *
 */
public class HttpAccessInterceptor extends AbstractHandlerInterceptor {
	//
	private static final Logger ACCESS = LoggerFactory.getLogger("ACCESS_LOGGER");
	private final static Logger LOGGER = LoggerFactory.getLogger(HttpAccessInterceptor.class);
	
	/**
	 * 
	 */
	public HttpAccessInterceptor() {
		super("web.advance.interceptor.access");
	}
	
	/**
	 * 
	 */
	@Override
	public final void afterCompletion( HttpServletRequest request, HttpServletResponse response, Object handler, Exception error )
	throws Exception {
		//
		if(!(handler instanceof HandlerMethod)) return;
		
		//
		try {
			final int status = response.getStatus();
			final String method = request.getMethod();
			final String ip = HttpRequests.getIp(request);
			final String path = HttpRequests.getAccessPath(request);
			final UserAgent agent = HttpRequests.getUserAgent(request);
			final String sessionId = HttpRequests.getSessionId(request, "");
			final String query = trimToEmpty(request.getQueryString());
			final String cause = error == null ? "" : getRootCauseMessage(error);
			ACCESS.info("{} {},{},{},{},{},{},{}", status, sessionId, ip, method, path, query, agent, cause);
		} catch(Throwable tx) {
			LOGGER.error("[" + this.name + "]failed to intercept, request: " + request + ", response: " + response, tx);
		}
	}
}
