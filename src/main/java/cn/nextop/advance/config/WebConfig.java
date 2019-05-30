package cn.nextop.advance.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.nextop.advance.interceptor.impl.AuthInterceptor;

/**
 * 
 * @author qutl
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	//
	@Value("${apple.web.aysnc.timeout}") private long timeout;
	
	/**
	 * Cors
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**");
		WebMvcConfigurer.super.addCorsMappings(registry);
	}
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		WebMvcConfigurer.super.configureMessageConverters(converters);
	}
	
	/**
	 * Async
	 */
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setDefaultTimeout(timeout);
		WebMvcConfigurer.super.configureAsyncSupport(configurer);
	}
	
	/**
	 * Interceptor
	 */
	@Bean
	public AuthInterceptor getAuthInterceptor() {
		return new AuthInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getAuthInterceptor()).addPathPatterns("/api/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}
}
