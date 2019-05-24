package cn.nextop.advance.controller.advice;

/**
 * 
 * @author qutl
 *
 */
public abstract class AbstractExceptionHandler {
	//
	protected final String name;
	
	/**
	 * 
	 */
	public AbstractExceptionHandler(String name) {
		this.name = "web.advance." + name;
	}
}
