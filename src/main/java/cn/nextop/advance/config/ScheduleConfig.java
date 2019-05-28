package cn.nextop.advance.config;

import static com.google.common.util.concurrent.UncaughtExceptionHandlers.systemExit;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/**
 * 
 * @author qutl
 *
 */
@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {
	//
	@Value("${apple.scheduler.threadpool}") private int poolSize;
	
    @Bean(destroyMethod="shutdown")
    public Executor getScheduler() {
    	UncaughtExceptionHandler handler = systemExit();
    	ThreadFactoryBuilder builder = new ThreadFactoryBuilder();
    	builder.setNameFormat("Schedule-").setUncaughtExceptionHandler(handler);
        return Executors.newScheduledThreadPool(poolSize, builder.build());
    }
    
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(getScheduler());
    }
}