package cn.nextop.advance.support.security;

/**
 * 
 * @author qutl
 *
 */
public final class SecurityContexts {
	//
	private static final ThreadLocal<Context> CONTEXT;
	public static final Context get() { return CONTEXT.get(); }
	public static final void set(Context c) { CONTEXT.set(c); }
	static { CONTEXT = new ThreadLocal<>(); }
	
	/**
	 * 
	 */
	public static final class Context {
		//
		private String sessionId;
		protected String accessToken;
		private final long timestamp = System.nanoTime();
		
		//
		public long getTimestamp() { return timestamp; }
		public String getSessionId() { return sessionId; }
		public String getAccessToken() { return accessToken; }
		public void setSessionId(String sessionId) { this.sessionId = sessionId; }
		public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
	}
	
	/**
	 * 
	 */
	public static final String getAccessToken() {
		final Context c = get(); return c == null ? null : c.getAccessToken();
	}
}
