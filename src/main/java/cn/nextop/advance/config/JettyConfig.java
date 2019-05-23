package cn.nextop.advance.config;

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

	/**
	 * 
	 */
	@Bean
	public JettyServletWebServerFactory jettyEmbeddedServletContainerFactory(JettyServerCustomizer jettyServerCustomizer) {
		JettyServletWebServerFactory factory = new JettyServletWebServerFactory();
		factory.addServerCustomizers(jettyServerCustomizer); return factory;
	}

	@Bean
	public JettyServerCustomizer jettyServerCustomizer() {
		return server -> { 
			final QueuedThreadPool qtp = server.getBean(QueuedThreadPool.class);
			qtp.setMaxThreads(100); qtp.setMinThreads(20); qtp.setIdleTimeout(60000); qtp.setName("JettyThreadPool");};
	}
}
