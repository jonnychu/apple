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

import cn.nextop.advance.interceptor.impl.HttpAccessInterceptor;
import cn.nextop.advance.interceptor.impl.HttpAuthenticationInterceptor;
import cn.nextop.advance.interceptor.impl.HttpAuthorizationInterceptor;

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
	
	/**
	 * Json convert
	 */
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
	 * Http Interceptor
	 */
	@Bean
	public HttpAccessInterceptor getAccessInterceptor() {
		return new HttpAccessInterceptor();
	}
	
	@Bean
	public HttpAuthorizationInterceptor getAuthorizationInterceptor() {
		return new HttpAuthorizationInterceptor();
	}
	
	@Bean
	public HttpAuthenticationInterceptor getAuthenticationInterceptor() {
		return new HttpAuthenticationInterceptor();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getAccessInterceptor()).order(3).addPathPatterns("/api/**");
		registry.addInterceptor(getAuthorizationInterceptor()).order(2).addPathPatterns("/api/**");
		registry.addInterceptor(getAuthenticationInterceptor()).order(1).addPathPatterns("/api/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}
}
