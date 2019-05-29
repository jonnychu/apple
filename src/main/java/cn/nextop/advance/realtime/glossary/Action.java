package cn.nextop.advance.realtime.glossary;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author qutl
 *
 */
public enum Action {
	//
	PING, SUBSCRIBE, UNSUBSCRIBE;
	
	/**
	 * 
	 */
	public static final Action valueOf (final String n, final Action v) {
		if(StringUtils.isEmpty(n)) return v;
		try { return valueOf(n); } catch (Throwable ignore) { return v; }
	}
}
