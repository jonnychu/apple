package cn.nextop.advance.support.realtime;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.eclipse.jetty.websocket.server.HandshakeRFC6455;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.springframework.context.Lifecycle;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.socket.WebSocketExtension;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.adapter.jetty.JettyWebSocketHandlerAdapter;
import org.springframework.web.socket.adapter.jetty.WebSocketToJettyExtensionConfigAdapter;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.RequestUpgradeStrategy;

import com.google.common.collect.Sets;

/**
 * 
 * @author qutl
 *
 */
public class XRequestUpgradeStrategy implements RequestUpgradeStrategy, ServletContextAware, Lifecycle {
	//
	private static final ThreadLocal<WebSocketHandlerContainer> CONTAINER = new NamedThreadLocal<>("WebSocketHandlerContainer");
	
	//
	private ServletContext context;
	private volatile WebSocketServerFactory factory;
	private volatile List<WebSocketExtension> supportedExtensions1;
	private volatile List<WebSocketExtension> supportedExtensions2;
	private final AtomicBoolean running = new AtomicBoolean(false);
	private WebSocketPolicy policy = WebSocketPolicy.newServerPolicy();
	private Set<String> exclusion = Sets.newHashSet("deflate-frame", "permessage-deflate", "x-webkit-deflate-frame"); /* compression */
	
	/**
	 * 
	 */
	public XRequestUpgradeStrategy() {
	}
	
	public XRequestUpgradeStrategy(WebSocketPolicy policy) {
		this.policy = policy;
	}
	
	public XRequestUpgradeStrategy(WebSocketServerFactory factory) {
		this.factory = factory;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean isRunning() {
		return this.running.get();
	}
	
	public Set<String> getExclusion() {
		return exclusion;
	}
	
	public void setExclusion(Set<String> exclusion) {
		this.exclusion = exclusion;
	}
	
	@Override
	public void setServletContext(ServletContext context) {
		this.context = context;
	}
	
	@Override
	public String[] getSupportedVersions() {
		return new String[] { String.valueOf(HandshakeRFC6455.VERSION) };
	}
	
	/**
	 * 
	 */
	@Override
	public void start() {
		if(!this.running.compareAndSet(false, true)) return;
		if((factory == null)) factory = new WebSocketServerFactory(context, policy);
		try {
			this.factory.setCreator(new WebSocketCreatorEx()); this.factory.start();
		} catch (Throwable tx) {
			throw new IllegalStateException("failed to start upgrade strategy", tx);
		}
	}
	
	@Override
	public void stop() {
		if(!this.running.compareAndSet(true, false)) return;
		try {
			if(this.factory != null && factory.isRunning()) { this.factory.stop(); }
		} catch (Throwable txt) {
			throw new IllegalStateException("failed to stop upgrade strategy", txt);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public List<WebSocketExtension> getSupportedExtensions (ServerHttpRequest request) {
		//
		final boolean compress = true;
		if(compress) {
			if (this.supportedExtensions1 != null) { return this.supportedExtensions1; }
		} else {
			if (this.supportedExtensions2 != null) { return this.supportedExtensions2; }
		}
		
		//
		List<WebSocketExtension> extensions1 = new ArrayList<WebSocketExtension>();
		List<WebSocketExtension> extensions2 = new ArrayList<WebSocketExtension>();
		for(String name : this.factory.getExtensionFactory().getExtensionNames()) {
			extensions1.add(new WebSocketExtension((name))); /* without any exclusion */
			if(!exclusion.contains(name)) extensions2.add(new WebSocketExtension(name));
		}
		supportedExtensions1 = Collections.unmodifiableList(extensions1);
		supportedExtensions2 = Collections.unmodifiableList(extensions2);
		if(compress) return supportedExtensions1; else return this.supportedExtensions2;
	}
	
	/**
	 * 
	 */
	@Override
	public void upgrade(ServerHttpRequest request, ServerHttpResponse response,
						String protocol, List<WebSocketExtension> extensions, Principal user,
						WebSocketHandler handler, Map<String, Object> attributes) throws HandshakeFailureException {
		//
		Assert.isInstanceOf(ServletServerHttpRequest.class, request);
		Assert.isInstanceOf(ServletServerHttpResponse.class, response);
		final HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
		final HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
		Assert.isTrue(this.factory.isUpgradeRequest(servletRequest, servletResponse), "expected websocket upgrade");
		
		//
		try {
			final XWebSocketSession session = new XWebSocketSession(attributes, user);
			session.setCapacity(((XWebSocketPolicy) this.factory.getPolicy()).getCapacity());
			final JettyWebSocketHandlerAdapter adapter = new JettyWebSocketHandlerAdapter(handler, session);
			final WebSocketHandlerContainer container = new WebSocketHandlerContainer(adapter, protocol, extensions);
			CONTAINER.set(container); factory.acceptWebSocket(servletRequest, servletResponse); // WebSocketCreatorEx
		} catch (IOException ex) {
			throw new HandshakeFailureException("failed to upgrade, request: " + request, ex);
		} finally {
			CONTAINER.remove();
		}
	}
	
	/**
	 * 
	 */
	private static class WebSocketCreatorEx implements WebSocketCreator {
		@Override public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
			final WebSocketHandlerContainer container = CONTAINER.get();
			Assert.state((container != null), "expected websocket handler container"); UpgradeResponse r = response;
			r.setExtensions(container.extensions); r.setAcceptedSubProtocol(container.protocol); return container.handler;
		}
	}
	
	/**
	 * 
	 */
	private static class WebSocketHandlerContainer {
		//
		private final String protocol;
		private final List<ExtensionConfig> extensions;
		private final JettyWebSocketHandlerAdapter handler;
		
		/**
		 * 
		 */
		public WebSocketHandlerContainer(JettyWebSocketHandlerAdapter handler, String protocol, List<WebSocketExtension> extensions) {
			this.handler = handler; this.protocol = protocol;
			if (CollectionUtils.isEmpty(extensions)) {
				this.extensions = new ArrayList<>(0);
			} else {
				this.extensions = new ArrayList<ExtensionConfig>(extensions.size());
				for(final WebSocketExtension we : extensions) { this.extensions.add(new WebSocketToJettyExtensionConfigAdapter(we)); }
			}
		}
	}
}

