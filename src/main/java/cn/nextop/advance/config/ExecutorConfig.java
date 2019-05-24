package cn.nextop.advance.config;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 
 * @author qutl
 *
 */
@Configuration
public class ExecutorConfig implements AsyncConfigurer {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorConfig.class);
	
	@Bean
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
		tpte.setThreadNamePrefix("AsyncExecutor-"); tpte.setCorePoolSize(2); 
		tpte.setMaxPoolSize(10); tpte.setQueueCapacity(20); tpte.initialize(); return tpte;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {
			
			@Override
			public void handleUncaughtException(Throwable ex, Method method, Object... params) {
				LOGGER.error("aysnc handled exception, throwable: {}, method: {}, params: {}", ex, method, params);
			}
		};
	}
}
