package cn.nextop.advance.config;

import org.springframework.context.annotation.Configuration;
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
	
	/**
	 * Cors
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/api/**");
		WebMvcConfigurer.super.addCorsMappings(registry);
	}
	
	/**
	 * Interceptor
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/api/customer/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}
	
	/**
	 * Async
	 */
	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setDefaultTimeout(6000);
		WebMvcConfigurer.super.configureAsyncSupport(configurer);
	}
}
