package cn.nextop.advance.realtime.glossary;

import com.google.common.base.Strings;

/**
 * 
 * @author qutl
 *
 */
public enum Event {
	//
	CUSTOMER;

	/**
	 * 
	 */
	public static final Event valueOf(final String n, final Event v) {
		if (Strings.isNullOrEmpty(n)) return v;
		try { return valueOf(n); } catch (Throwable ignore) { return v; }
	}
}
