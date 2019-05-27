package cn.nextop.advance.interceptor.impl;

import java.util.concurrent.Callable;

import org.springframework.web.context.request.NativeWebRequest;

import cn.nextop.advance.interceptor.AbstractAsyncInterceptor;

public class AsyncInterceptor extends AbstractAsyncInterceptor {

	@Override
	public <T> Object handleTimeout(NativeWebRequest request, Callable<T> task) throws Exception {
		System.out.println("22222222222222222222222222222");
		return super.handleTimeout(request, task);
	}
}
