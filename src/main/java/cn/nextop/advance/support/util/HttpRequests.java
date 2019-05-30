package cn.nextop.advance.support.util;

import static nl.bitwalker.useragentutils.UserAgent.parseUserAgentString;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;

import nl.bitwalker.useragentutils.UserAgent;

/**
 * 
 * @author qutl
 *
 */
@SuppressWarnings("unchecked")
public final class HttpRequests {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequests.class);
	
	//
	public static final String API = "/api/";
	public static final String BEARER = "Bearer ";
	public static final String X_VERSION = "X-Version";
	public static final String ACCESS_TOKEN = "access_token";
	
	//
	public static final String SESSION_ID_KEY = "session_id";
	
	//
	public static final String LOGIN_KEY = "_login_";
	public static final String CONTEXT_KEY = "_context_";
	public static final String CSRF_TOKEN_KEY = "_csrf_token_";
	public static final String OPERATOR_PRIVILEGE_KEY = "_operator_privilege_";
	
	/**
	 * 
	 */
	public static final String getIp(HttpServletRequest request) {
		try {
			//
			String ip = request.getHeader("X-Forwarded-For");
			if (isEmpty(ip) || "unknown".equalsIgnoreCase(ip.trim())) {
				ip = request.getHeader("X-Real-IP");
			} else {
				ip = ip.split(",")[0].trim();
			}
			
			//
			if (isEmpty(ip) || "unknown".equalsIgnoreCase(ip.trim())) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			
			//
			if (isEmpty(ip) || "unknown".equalsIgnoreCase(ip.trim())) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			
			//
			if (isEmpty(ip) || "unknown".equalsIgnoreCase(ip.trim())) {
				ip = request.getRemoteAddr();
			}
			
			//
			if (isEmpty(ip) || "unknown".equalsIgnoreCase(ip.trim())) {
				final String id = getSessionId(request, "");
				LOGGER.warn("failed to get ip, sessionId {}", id);
			}
			return ip;
		} catch (Throwable t) {
			final String id = getSessionId(request, "");
			LOGGER.error("failed to get ip, sessionId: " + id, t); return null;
		}
	}
	
	/**
	 * 
	 */
	public static final <T> T get (HttpSession session, String n) {
		return session == null ? null : (T)session.getAttribute(n);
	}
	
	public static <T> T get(HttpServletRequest request, String n) {
		return request == null ? null : (T)request.getAttribute(n);
	}
	
	public static String getAccessPath(HttpServletRequest request) {
		if(request == null) return ""; return request.getRequestURI();
	}
	
	public static UserAgent getUserAgent(HttpServletRequest request) {
		return parseUserAgentString (request.getHeader("User-Agent"));
	}
	
	public static String getCsrfToken (final HttpServletRequest request) {
		final String r = request.getHeader("X-Csrf-Token"); /* first, try header */
		return !isEmpty(r) ? r : request.getParameter(HttpRequests.CSRF_TOKEN_KEY);
	}
	
	public static String getAccessPath (HttpServletRequest request, boolean trim) {
		final String result = (request == null) ? "" : request.getRequestURI();
		return trim ? result.substring(request.getContextPath().length()) : result;
	}
	
	public static final String getVersion(final HttpServletRequest request) {
		String v = getAccessPath(request); if (!v.startsWith(API)) return request.getHeader(X_VERSION);
		final int st = API.length(), ed = v.indexOf('/', st); return ed > 0 ? v.substring(st, ed) : "";
	}
	
	public static String getAccessToken(HttpServletRequest request) {
		// see http://self-issued.info/docs/draft-ietf-oauth-v2-bearer.html
		String token = null, auth = trimToNull(request.getHeader(AUTHORIZATION));
		if (auth == null) token = trimToNull(request.getParameter(ACCESS_TOKEN));
		else if(auth.startsWith(BEARER)) token = trimToNull(auth.substring(BEARER.length())); return token;
	}
	
	public static Cookie getCookie(HttpServletRequest request, final String name) {
		Cookie cookies[] = request == null ? null : request.getCookies(); if (cookies == null) return null;
		for(int i = 0; i < cookies.length; i++) if(StringUtils.equals(cookies[i].getName(), name)) return cookies[i];
		return null;
	}
	
	/**
	 * 
	 */
	public static String getSessionId(HttpServletRequest request, final String v) {
		HttpSession r = request.getSession(false); if((r == null)) return v; else return r.getId();
	}
	
	/**
	 * 
	 */
	public static String toShortString(HttpServletRequest request) {
		return new StringBuilder(128).append("ip=").append(getIp(request)).append(",uri=").append(request.getRequestURI())
		.toString();
	}
	
	public static final String toString(HttpServletRequest request) {
		return new StringBuilder(256).append("ip=").append(getIp(request)).append(",uri=").append(request.getRequestURI())
		.append(",ua=").append(request.getHeader("User-Agent")).append(",csrf=").append(getCsrfToken(request)).toString();
	}
	
	public static final String toString(final ServerHttpRequest request) {
		final StringBuilder r = new StringBuilder(512); return r.append("ip=").append(request.getRemoteAddress()).append(",method=")
		.append(request.getMethod()).append(",principal=").append(request.getPrincipal()).append(",headers=").append(request.getHeaders()).toString();
	}
}
