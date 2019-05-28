package cn.nextop.advance.support.exception;

/**
 * 
 * @author qutl
 *
 */
public class AssertionException extends RuntimeException {
	//
	private static final long serialVersionUID = 7066468784796847964L;
	
	/**
	 * 
	 */
	public AssertionException() {
		this(null, null);
	}
	
	public AssertionException(String msg) {
		this(msg, null);
	}
	
	public AssertionException(Throwable cause) {
		this(null, cause);
	}
	
	public AssertionException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
