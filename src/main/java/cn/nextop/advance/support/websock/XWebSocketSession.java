package cn.nextop.advance.support.websock;


import java.net.InetSocketAddress;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.adapter.jetty.JettyWebSocketSession;

import cn.nextop.advance.support.exception.AssertionException;
import cn.nextop.advance.support.exception.OverloadException;
import cn.nextop.advance.support.util.SessionGenerator;

/**
 * 
 * @author qutl
 *
 */
public class XWebSocketSession extends JettyWebSocketSession {
	//
	private String id;
	private int capacity = 0;
	private InetSocketAddress address;
	private final AtomicInteger messages = new AtomicInteger (0);
	private volatile long timestamp = System.currentTimeMillis();
	
	/**
	 * 
	 */
	public XWebSocketSession(Map<String, Object> attributes) {
		this(attributes, null);
	}
	
	public XWebSocketSession(Map<String, Object> attributes, Principal user) {
		super(attributes, user);
	}
	
	/**
	 * 
	 */
	@Override
	public String getId() {
		return this.id;
	}
	
	public int getCapacity() {
		return capacity;
	}
	
	public long getTimestamp() {
		return this.timestamp;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public final InetSocketAddress getRemoteAddress () {
		if(this.address != null) return this.address;
		return super.getRemoteAddress(); /* fail-over */
	}
	
	protected String getHeader (String key, int index) {
		List<String> r = getHandshakeHeaders().get(key);
		return (r == null) ? null : r.get(index);
	}
	
	@Override
	public void initializeNativeSession(final Session session) {
		// session id
		super.initializeNativeSession(session);
		this.id = SessionGenerator.getDefault().next();
		// real ip TODO
	}
	
	/**
	 * 
	 */
	public void sendMessage(final WebSocketMessage<?> message, WriteCallback callback) {
		//
		final int counter = this.messages.incrementAndGet();
		final WriteCallbackEx adapter = new WriteCallbackEx(callback);
		if(!(message instanceof TextMessage)) { adapter.writeFailed(new AssertionException()); return; }
		if(capacity > 0 && counter > capacity) { adapter.writeFailed(new OverloadException()); return; }
		
		//
		final Session session = getNativeSession(); final String m = ((TextMessage) message).getPayload();
		try { session.getRemote().sendString(m, adapter); } catch(Throwable t) { adapter.writeFailed(t); }
	}
	
	/**
	 * 
	 */
	protected class WriteCallbackEx implements WriteCallback {
		protected final WriteCallback callback; public WriteCallbackEx(WriteCallback callback) { this.callback = callback; }
		@Override public void writeSuccess() { messages.decrementAndGet(); if(this.callback != null) this.callback.writeSuccess(); }
		@Override public void writeFailed(Throwable x) { messages.decrementAndGet(); if(callback != null) callback.writeFailed(x); }
	}
}
