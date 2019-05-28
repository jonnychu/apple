package cn.nextop.advance.realtime;

import java.util.Map;

import cn.nextop.advance.realtime.glossary.Event;
import cn.nextop.advance.support.websock.XWebSocketSession;

/**
 * 
 * @author qutl
 *
 */
public interface XWebSocketChannel {
	
	/**
	 * 
	 */
	interface Subscriber {
		
		String getSessionId();
		
		XWebSocketSession getSession();
		
		<T extends XWebSocketSubscription> T getSubscription();
	}
	
	/**
	 * 
	 */
	String getName(); Event getEvent();
	
	void subscribe(XWebSocketSession wss, Map<String, Object> args);
	
	void unsubscribe(XWebSocketSession wss, Map<String, Object> args);
}
