package cn.nextop.advance.controller;

public abstract class AbstractController {
	//
	protected final String name;
	
	/**
	 * 
	 */
	public AbstractController(String name) {
		this.name = "web.advance.controller." + name;
	}
}
