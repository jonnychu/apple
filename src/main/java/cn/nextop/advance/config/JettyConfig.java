package cn.nextop.advance.config;

import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.springframework.beans.factory.annotation.Value;
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
	//
	@Value("${apple.jetty.minThread}") private int minThread;
	@Value("${apple.jetty.maxThread}") private int maxThread;
	@Value("${apple.jetty.idleTimeout}") private int idleTimeout;

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
			QueuedThreadPool qtp = server.getBean(QueuedThreadPool.class); qtp.setMaxThreads(maxThread); 
			qtp.setMinThreads(minThread); qtp.setIdleTimeout(idleTimeout); qtp.setName("JettyThreadPool");};
	}
}
