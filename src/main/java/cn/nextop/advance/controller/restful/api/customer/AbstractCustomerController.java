package cn.nextop.advance.controller.restful.api.customer;

import org.springframework.web.bind.annotation.RequestMapping;

import cn.nextop.advance.controller.AbstractController;

/**
 * 
 * @author qutl
 *
 */
@RequestMapping("/api/customer")
public class AbstractCustomerController extends AbstractController {

	/**
	 * 
	 */
	public AbstractCustomerController(String name) {
		super("customer." + name);
	}

}
