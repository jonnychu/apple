package cn.nextop.advance.support.websock;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static org.springframework.web.socket.CloseStatus.NORMAL;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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

/**
 * 
 * @author qutl
 *
 */
@Component
public class XWebSocketHandler implements WebSocketHandler {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(XWebSocketHandler.class);
	//
	private final ConcurrentMap<String, WebSocketSession> sessions;
	
	/**
	 * 
	 */
	public XWebSocketHandler() {
		this.sessions = new ConcurrentHashMap<>(4096);
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		final String sessionId = session.getId(); System.out.println(sessionId);
		if ((message instanceof PingMessage) || (message instanceof PongMessage)) return; /* ignore ping/pong messages */
		try {
			session.sendMessage(new TextMessage("hello!!!"));
		} catch(Throwable t) {
			LOGGER.error("en.........");
		}
	}
	
	/**
	 * 
	 */
	@Scheduled(cron = "0/15 * * * * *")
	protected void pulse() {
		final long now = System.currentTimeMillis(), mark = System.nanoTime();
		final long timeout = 30000L; System.out.println("pulse");
		try {
			if (timeout > 0L) for (WebSocketSession wss : this.sessions.values()) {
				if(((XWebSocketSession)wss).getTimestamp() + timeout >= now) continue;
				final WebSocketSession expire = this.sessions.remove(wss.getId());
				LOGGER.info("[WEBSOCKET]expired session: {}", wss.getId());
				try { if (expire != null) ((XWebSocketSession) (expire)).close(NORMAL); } catch (Throwable ignore) {}
			}
		} catch(Throwable cause) {
			long et = MILLISECONDS.convert((System.nanoTime() - mark), NANOSECONDS);
			LOGGER.error("failed to pulse, elapsed time: " + et + " ms", cause);
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
