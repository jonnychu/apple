package cn.nextop.advance.support.websock;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang3.StringUtils.upperCase;
import static org.springframework.web.socket.CloseStatus.NORMAL;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import cn.nextop.advance.realtime.XWebSocketChannel;
import cn.nextop.advance.realtime.glossary.Action;
import cn.nextop.advance.realtime.glossary.Event;
import cn.nextop.advance.realtime.glossary.Result;
import cn.nextop.advance.support.util.ConcurrentMultiKeyMap;
import cn.nextop.advance.support.util.HttpRequests;
import cn.nextop.advance.support.util.MapperUtil;

/**
 * 
 * @author qutl
 *
 */
@Component
public class XWebSocketHandler implements WebSocketHandler {
	//
	private static final Logger WEBSOCKET = LoggerFactory.getLogger("WEBSOCKET_LOGGER");
	private static final Logger LOGGER = LoggerFactory.getLogger(XWebSocketHandler.class);
	//
	private final ConcurrentMultiKeyMap<String, String, WebSocketSession> sessions;
	private final Map<Event, XWebSocketChannel> channels2 = new EnumMap<>(Event.class);
	
	/**
	 * 
	 */
	public XWebSocketHandler() {
		this.sessions = new ConcurrentMultiKeyMap<>(4096);
	}

	public void addChannels (Event event, XWebSocketChannel channel) {
		this.channels2.put(event, channel);
	}
	
	protected final String getSessionId(final WebSocketSession session) {
		return (String) (session.getAttributes().get(HttpRequests.SESSION_ID_KEY));
	}
	
	protected final void reply(XWebSocketSession session, Map<String, Object> request, Result result) {
		final Map<String, Object> r = request; /* reply all request entries */ r.put("result", result);
		session.sendMessage(new TextMessage(MapperUtil.write(r)), XWriteCallback.callback (null, (cause) -> {
			WEBSOCKET.info ("! {},{},{},{}", session.getId(), getSessionId(session), session.getRemoteAddress().getHostString(), cause);
			try { session.close(NORMAL); } catch (Throwable ignore) { LOGGER.error("[WEBSOCKET]failed to close: " + session.getId(), ignore); }
		}));
	}
	
	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		long et, mark = System.nanoTime(); Map<String, Object> m = null; String payload = null;
		final String sessionId = session.getId();
		if (session instanceof XWebSocketSession) ((XWebSocketSession) session).setTimestamp(System.currentTimeMillis());
		if ((message instanceof PingMessage) || (message instanceof PongMessage)) return; /* ignore ping/pong messages */
		try {
			//
			if(!(message instanceof TextMessage)) {
				LOGGER.warn("[WEBSOCKET]reject message[1]: {}, sessionId: {}", message, sessionId); return;
			}
			
			payload = (String)message.getPayload(); if (payload == null) {
				WEBSOCKET.info("* {}", session.getId()); /*ping*/
				LOGGER.warn("[WEBSOCKET]reject message[2]: {}, sessionId: {}", payload, sessionId); return;
			}
			
			//
			m = MapperUtil.read(payload);
			Action action = Action.valueOf(upperCase((String)m.get("action")), null); if (action == null) {
				reply((XWebSocketSession)session, m, Result.BAD_REQUEST);
				LOGGER.warn("[WEBSOCKET]reject message[4]: {}, sessionId: {}", payload, sessionId); return;
			}
			
			//
			if (action == Action.PING) {
				reply ((XWebSocketSession)session, m, Result.SUCCESS);
				WEBSOCKET.info("* {}", session.getId());/* ping */return;
			}
			final Event event = Event.valueOf(upperCase((String)(m.get("event"))), null); if(event == null) {
				reply((XWebSocketSession)session, m, Result.BAD_REQUEST);
				LOGGER.warn("[WEBSOCKET]reject message[5]: {}, sessionId: {}", payload, sessionId); return;
			}
			
			//
			final XWebSocketSession wss = (XWebSocketSession)session;
			final XWebSocketChannel wsc = this.channels2.get(event);
			if(action == Action.SUBSCRIBE) wsc.subscribe(wss, m); else if(action == Action.UNSUBSCRIBE) wsc.unsubscribe(wss, m);
			et = System.nanoTime() - mark;
			LOGGER.info("[WEBSOCKET]handle: {}, sessionId: {}, elapsed time: {} ms", payload, sessionId, MILLISECONDS.toNanos(et));
		} catch(Throwable t) {
			reply((XWebSocketSession)session, (m), Result.INTERNAL_ERROR);
			LOGGER.warn("[WEBSOCKET]failed to handle: {}, sessionId: {}, cause: {}", new Object[] { payload, sessionId, t });
		}
	}
	
	/**
	 * 
	 */
	@Scheduled(cron = "0/15 * * * * *")
	protected void pulse() {
		final long now = System.currentTimeMillis(), mark = System.nanoTime();
		final long timeout = 30000L;
		try {
			if (timeout > 0L) for(final String key : this.sessions.keySet()) {
				for(final WebSocketSession session : this.sessions.get((key)).values()) {
					//
					if(((XWebSocketSession) session).getTimestamp() + timeout >= now) continue;
					final WebSocketSession expire = this.sessions.remove(key, session.getId());
					LOGGER.info("[WEBSOCKET]expired session: {}", session.getId());
					
					//
					final String sessionId = getSessionId(session);
					String address = session.getRemoteAddress().getHostString();
					WEBSOCKET.info("! {},{},{},{},{}", session.getId(), sessionId, address, "expire");
					try { if (expire != null) ((XWebSocketSession)(expire)).close(NORMAL); } catch(Throwable ignore) {}
				}
			}
		} catch(Throwable cause) {
			LOGGER.error("failed to pulse, elapsed time: " + TimeUnit.MILLISECONDS.toNanos(System.nanoTime() - mark) + " ms", cause);
		}
	}

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
		
	}

	@Override
	public boolean supportsPartialMessages() { return false; }
}
