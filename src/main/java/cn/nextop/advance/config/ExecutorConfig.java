package cn.nextop.advance.config;

import static java.time.Duration.ofSeconds;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 
 * @author qutl
 *
 */
@EnableAsync
@Configuration
public class ExecutorConfig implements AsyncConfigurer {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorConfig.class);
	//
	@Value("${apple.executor.keeplive}") private int keeplive;
	@Value("${apple.executor.pool.maxSize}") private int maxSize;
	@Value("${apple.executor.pool.coreSize}") private int coreSize;
	@Value("${apple.executor.queue.capacity}") private int capacity;
	
	@Bean
	@Override
	public Executor getAsyncExecutor() {
		TaskExecutorBuilder builder = new TaskExecutorBuilder();
		builder.corePoolSize(coreSize).maxPoolSize(maxSize).queueCapacity(capacity)
		.keepAlive(ofSeconds(keeplive)).threadNamePrefix("AsyncThread-"); return builder.build();
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
