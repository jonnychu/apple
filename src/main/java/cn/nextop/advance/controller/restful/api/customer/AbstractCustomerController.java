package cn.nextop.advance.controller.restful.api.customer;

import cn.nextop.advance.controller.AbstractController;

/**
 * 
 * @author qutl
 *
 */
public class AbstractCustomerController extends AbstractController {

	/**
	 * 
	 */
	public AbstractCustomerController(String name) {
		super("customer." + name);
	}

}
