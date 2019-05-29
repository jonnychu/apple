package cn.nextop.advance.realtime.channel.api;

import cn.nextop.advance.realtime.channel.AbstractChannel;
import cn.nextop.advance.realtime.glossary.Event;

/**
 * 
 * @author qutl
 *
 */
public abstract class AbstractApiChannel extends AbstractChannel {
	
	/**
	 * 
	 */
	public AbstractApiChannel(String name, Event event) {
		super("api.advance.channel." + name, event);
	}
}
