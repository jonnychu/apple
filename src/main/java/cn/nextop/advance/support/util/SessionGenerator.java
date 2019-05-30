package cn.nextop.advance.support.util;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * 
 * @author qutl
 *
 */
public class SessionGenerator {
	//
	public static final SessionGenerator getDefault() { return DEFAULT; }
	private static final SessionGenerator DEFAULT = new SessionGenerator();
	
	public String next() {
		return RandomStringUtils.random(36, true, true);
	}
}
