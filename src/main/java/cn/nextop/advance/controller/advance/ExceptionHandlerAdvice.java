package cn.nextop.advance.controller.advance;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author qutl
 *
 */
@ControllerAdvice
public class ExceptionHandlerAdvice extends AbstractExceptionHandler {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

	/**
	 * 
	 */
	public ExceptionHandlerAdvice() {
		super("exception.handler");
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public String handle(HttpServletRequest request, Exception e) {
		return "error";
	}
}
