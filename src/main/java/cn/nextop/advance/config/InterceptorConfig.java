package cn.nextop.advance.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.nextop.advance.interceptor.impl.AuthInterceptor;

/**
 * 
 * @author qutl
 *
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new AuthInterceptor()).addPathPatterns("/api/customer/**");
		WebMvcConfigurer.super.addInterceptors(registry);
	}
}
