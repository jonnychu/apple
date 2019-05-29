package cn.nextop.advance.realtime.channel;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.eclipse.jetty.websocket.api.WriteCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;

import com.google.common.collect.Maps;

import cn.nextop.advance.realtime.XWebSocketChannel;
import cn.nextop.advance.realtime.XWebSocketSubscription;
import cn.nextop.advance.realtime.glossary.Event;
import cn.nextop.advance.support.util.ConcurrentMultiKeyMap;
import cn.nextop.advance.support.util.HttpRequests;
import cn.nextop.advance.support.util.MapperUtil;
import cn.nextop.advance.support.websock.XWebSocketSession;

/**
 * 
 * @author qutl
 *
 */
public abstract class AbstractChannel implements XWebSocketChannel {
	//
	private static final Logger EVENT = LoggerFactory.getLogger("EVENT_LOGGER");
	private static final Logger WEBSOCKET = LoggerFactory.getLogger("WEBSOCKET_LOGGER");
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractChannel.class);
	
	//
	protected final String name; @Override public final String getName() { return  this.name; }
	protected final Event event; @Override public final Event getEvent() { return this.event; }
	protected final ConcurrentMultiKeyMap<String, XWebSocketSubscription, SubscriberEx> index1;
	protected final ConcurrentMultiKeyMap<XWebSocketSubscription, String, SubscriberEx> index2;
	
	/**
	 * 
	 */
	public AbstractChannel(final String name, Event event) {
		this.name = name; this.event = event;
		this.index1 = new ConcurrentMultiKeyMap<>(4096);
		this.index2 = new ConcurrentMultiKeyMap<>(4096);
	}
	
	/**
	 * 
	 */
	protected final boolean isEmpty(Map<?, ?> m) {
		if(m == null || m.isEmpty()) return true; else return false;
	}
	
	protected final String string(Object v, String w) {
		return v != null && v instanceof String ? (String) v : w;
	}
	
	protected final Integer int32(Object v, Integer w) {
		return v != null && v instanceof Number ? ((Number)v).intValue() : w;
	}
	
	protected final Byte int8(final Object v, Byte w) {
		return v != null && v instanceof Number ? ((Number)v).byteValue() : w;
	}
	
	protected final Long int64(final Object v, Long w) {
		return v != null && v instanceof Number ? ((Number)v).longValue() : w;
	}
	
	protected final Short int16(final Object v, Short w) {
		return v != null && v instanceof Number ? ((Number)v).shortValue() : w;
	}
	
	/**
	 * 
	 */
	protected void unsubscribe(SubscriberEx s, Map<String, Object> args) {
		if (s == null) { return;/* NOP */} final String sid = s.getSessionId();
		try { unsubscribe(this, s, (args)); } catch (Throwable ignore) { /* NOP */ }
		WEBSOCKET.info ("- {},{},{}", sid, this.event, s.getSubscription());
	}
	
	@Override
	public final void subscribe( XWebSocketSession wss, Map<String, Object> args ) {
		XWebSocketSubscription v;
		final SubscriberEx w = new SubscriberEx(wss, (v = resolve(wss, args)));
		final String sid = w.getSessionId();
		subscribe(this, w, args); this.index1.put(sid, v, w); index2.put(v, sid, w);
		WEBSOCKET.info ("+ {},{},{}", sid, this.event, w.getSubscription());
	}
	
	@Override
	public final void unsubscribe(XWebSocketSession wss, Map<String, Object> args) {
		final String sessionId = wss.getId(); if (args != null) {
			XWebSocketSubscription v = resolve(wss, args); SubscriberEx s1, s2;
			s1 = index1.remove(sessionId, v); s2 = index2.remove(v, sessionId);
			if((s1 == null || s2 != null)) unsubscribe (s1 == null ? s2 : s1, args);
		} else {
			Map<XWebSocketSubscription, SubscriberEx> m = index1.remove (sessionId);
			if(m != null && !m.isEmpty()) for(SubscriberEx v : m.values()) {
				index2.remove(v.getSubscription(), sessionId); unsubscribe(v, args);
			}
		}
	}
	
	/**
	 * 
	 */
	protected static final String getSessionId(XWebSocketSession session) {
		return (String) session.getAttributes().get(HttpRequests.SESSION_ID_KEY);
	}
	
	protected XWebSocketSubscription resolve(XWebSocketSession wss, Map<String, Object> req) {
		throw new UnsupportedOperationException();
	}
	
	protected void subscribe ( XWebSocketChannel wsc, SubscriberEx sub, Map<String, Object> req ) {
		success(sub.getSession(), req, null);
	}
	
	protected void unsubscribe (XWebSocketChannel wsc, SubscriberEx sub, Map<String, Object> req) {
		success(sub.getSession(), req, null);
	}
	
	protected void success(XWebSocketSession wss, Map<String, Object> req, Object data) {
		Map<String, Object> rep = req; rep.put("result", "SUCCESS"); rep.put("data", data);
		wss.sendMessage(new TextMessage(MapperUtil.write(rep)), new WriteCallbackEx(wss));
	}
	
	/**
	 * 
	 */
	protected TextMessage text(Event event, Object data) {
		final String json = toJson(event, data); return new TextMessage(json);
	}
	
	protected TextMessage text(Event event, Object data, Map<String, Object> extra) {
		final String json = toJson(event, data, extra); return new TextMessage(json);
	}
	
	protected final String toJson( Event event, Object data ) {
		final Map<String, Object> message = Maps.newHashMapWithExpectedSize(2);
		message.put("event", event); message.put("data", data); return MapperUtil.write(message);
	}
	
	protected final String toJson (final Event event, final Object data, Map<String, Object> extra) {
		final Map<String, Object> message = Maps.newHashMapWithExpectedSize(2 + extra.size());
		for(Map.Entry<String, Object> me : extra.entrySet()) message.put(me.getKey(), me.getValue());
		message.put("event", event); message.put("data", data); return MapperUtil.write(message);
	}
	
	/**
	 * 
	 */
	protected final void broadcast(WebSocketMessage<?> message) {
		for (String key : this.index1.keySet()) { /* iteration */
			Map<XWebSocketSubscription, SubscriberEx> v = index1.get(key); if(isEmpty(v)) return;
			for(final Subscriber subscriber : v.values()) { push(subscriber, message); } /* push */
		}
	}
	
	protected void broadcast(WebSocketMessage<?> message, Predicate<XWebSocketSubscription> test) {
		for (String key : this.index1.keySet()) { /* iteration */
			Map<XWebSocketSubscription, SubscriberEx> v = index1.get(key); if(isEmpty(v)) return;
			for(final Entry<XWebSocketSubscription, SubscriberEx> me : v.entrySet()) {
				final SubscriberEx s = me.getValue(); if (test.test(me.getKey())) push(s, message);
			}
		}
	}
	
	protected int push(XWebSocketSubscription subscription, WebSocketMessage<?> message) {
		ConcurrentMap<String, SubscriberEx> m = index2.get(subscription); if (isEmpty(m)) return 0;
		int r = 0; for(final Subscriber sub : m.values()) { push(sub, message); r += 1; } return r;
	}
	
	protected int push(XWebSocketSubscription subscription, Supplier<WebSocketMessage<?>> message){
		ConcurrentMap<String, SubscriberEx> m = index2.get(subscription); if (isEmpty(m)) return 0;
		int r = 0; for(Subscriber sub : m.values()) { push(sub, message.get()); r += 1; } return r;
	}
	
	protected final void push (final Subscriber subscriber, WebSocketMessage<?> message) {
		if (subscriber == null || message == null) return; /* ignore if message is null */
		XWebSocketSession ws = subscriber.getSession();	ws.sendMessage(message, new WriteCallbackEx(ws)); 
		String sid = subscriber.getSession().getId(); EVENT.info("{},{},{}", new Object[]{event, sid, message.getPayload()});
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	public class SubscriberEx implements Subscriber {
		//
		protected final XWebSocketSession session;
		protected final XWebSocketSubscription subscription;
		
		//
		public SubscriberEx(final XWebSocketSession v, final XWebSocketSubscription w) {
			session = v; subscription = w;
		}
		
		//
		@Override public final String getSessionId() { return session.getId(); }
		@Override public final XWebSocketSession getSession() { return this.session; }
		@Override public final <T extends XWebSocketSubscription> T getSubscription() { return (T) this.subscription; }
	}
	
	/**
	 * 
	 */
	protected final class WriteCallbackEx implements WriteCallback {
		//
		protected final XWebSocketSession session;
		protected final long mark = System.nanoTime();
		public WriteCallbackEx (XWebSocketSession ws) { this.session = ws; }
		
		//
		@Override public void writeSuccess() { }
		
		@Override public void writeFailed(final Throwable cause) {
			LOGGER.error("fail to write, cause : " + cause);
		}
	}
}
