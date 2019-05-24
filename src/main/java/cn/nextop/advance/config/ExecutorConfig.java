package cn.nextop.advance.config;

import static java.time.Duration.ofSeconds;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

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
		TaskExecutorBuilder builder = new TaskExecutorBuilder();
		builder.corePoolSize(2).maxPoolSize(16).queueCapacity(100)
		.keepAlive(ofSeconds(60)).threadNamePrefix("AsyncThread-"); return builder.build();
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
