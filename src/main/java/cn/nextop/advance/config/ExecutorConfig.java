package cn.nextop.advance.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 
 * @author qutl
 *
 */
@EnableAsync
@Configuration
public class ExecutorConfig implements AsyncConfigurer {
	
	@Bean
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
		tpte.setThreadNamePrefix("AsyncExecutor-"); tpte.setCorePoolSize(2); 
		tpte.setMaxPoolSize(10); tpte.setQueueCapacity(20); tpte.initialize(); return tpte;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return null;
	}
}
