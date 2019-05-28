package cn.nextop.advance.support.exception;

/**
 * 
 * @author qutl
 *
 */
public class OverloadException extends RuntimeException {
	//
	private static final long serialVersionUID = -8596896786964431264L;
	
	/**
	 * 
	 */
	public OverloadException() {
		this(null, null);
	}
	
	public OverloadException(String msg) {
		this(msg, null);
	}
	
	public OverloadException(Throwable cause) {
		this(null, cause);
	}
	
	public OverloadException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
