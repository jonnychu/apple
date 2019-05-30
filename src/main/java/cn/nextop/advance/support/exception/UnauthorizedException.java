package cn.nextop.advance.support.exception;

/**
 * 
 * @author qutl
 *
 */
public class UnauthorizedException extends RuntimeException {
	//
	private static final long serialVersionUID = 6793759234895732507L;
	
	/**
	 * 
	 */
	public UnauthorizedException() {
		this(null, null);
	}
	
	public UnauthorizedException(String msg) {
		this(msg, null);
	}
	
	public UnauthorizedException(Throwable cause) {
		this(null, cause);
	}
	
	public UnauthorizedException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
