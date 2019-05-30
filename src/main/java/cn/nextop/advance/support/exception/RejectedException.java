package cn.nextop.advance.support.exception;

/**
 * 
 * @author qutl
 *
 */
public class RejectedException extends RuntimeException {
	//
	private static final long serialVersionUID = 8431951384010166867L;
	
	/**
	 * 
	 */
	public RejectedException() {
		this(null, null);
	}
	
	public RejectedException(String msg) {
		this(msg, null);
	}
	
	public RejectedException(Throwable cause) {
		this(null, cause);
	}
	
	public RejectedException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
