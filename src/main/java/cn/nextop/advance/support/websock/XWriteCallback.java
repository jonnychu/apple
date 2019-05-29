package cn.nextop.advance.support.websock;

import java.util.function.Consumer;

import org.eclipse.jetty.websocket.api.WriteCallback;

/**
 * 
 * @author qutl
 *
 */
public final class XWriteCallback implements WriteCallback {
	//
	private volatile Runnable success;
	private volatile Consumer<Throwable> failure;
	
	/**
	 * 
	 */
	@Override
	public void writeSuccess() {
		if(this.success != null) this.success.run();
	}
	
	@Override
	public void writeFailed(Throwable cause) {
		if(this.failure != null) this.failure.accept(cause);
	}
	
	/**
	 * 
	 */
	public static XWriteCallback callback(Runnable success, Consumer<Throwable> failure) {
		XWriteCallback r = new XWriteCallback(); r.success = success; r.failure = failure;
		return r;
	}
}
