package cn.nextop.advance.support.realtime;

import org.eclipse.jetty.websocket.api.WebSocketBehavior;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;

/**
 * 
 * @author qutl
 *
 */
public class XWebSocketPolicy extends WebSocketPolicy {
	//
	private int capacity;
	
	/**
	 * 
	 */
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	/**
	 * 
	 */
	public XWebSocketPolicy(WebSocketBehavior behavior) {
		super(behavior);
	}
}
