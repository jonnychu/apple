package cn.nextop.advance.config;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 
 * @author qutl
 *
 */
@Configuration
public class JettyConfig {

	@Bean
	public JettyServletWebServerFactory jettyEmbeddedServletContainerFactory(JettyServerCustomizer jettyServerCustomizer) {
		JettyServletWebServerFactory factory = new JettyServletWebServerFactory();
		factory.addServerCustomizers(jettyServerCustomizer); return factory;
	}

	@Bean
	public JettyServerCustomizer jettyServerCustomizer() {
		return server -> { threadPool(server); };
	}

	private void threadPool(Server server) {
		final QueuedThreadPool threadPool = server.getBean(QueuedThreadPool.class);
		threadPool.setMaxThreads(100); threadPool.setMinThreads(20); threadPool.setIdleTimeout(60000);
	}
}
