package cn.nextop.advance.realtime.glossary;

import org.apache.commons.lang3.StringUtils;

/**
 * 
 * @author qutl
 *
 */
public enum Result {
	//
	SUCCESS, OVERLOAD, REJECTED, RESTRICTED, BAD_REQUEST, INTERNAL_ERROR;
	
	/**
	 * 
	 */
	public static final Result valueOf (final String n, final Result v) {
		if(StringUtils.isEmpty(n)) return v;
		try { return valueOf(n); } catch (Throwable ignore) { return v; }
	}
}
