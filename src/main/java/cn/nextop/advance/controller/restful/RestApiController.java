package cn.nextop.advance.controller.restful;

import cn.nextop.advance.controller.AbstractController;

public abstract class RestApiController extends AbstractController {
	
	/**
	 * 
	 */
	public RestApiController(String name) {
		super("api." + name);
	}
}
