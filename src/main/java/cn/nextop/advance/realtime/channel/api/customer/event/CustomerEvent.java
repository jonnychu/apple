package cn.nextop.advance.realtime.channel.api.customer.event;

/**
 * 
 * @author qutl
 *
 */
public class CustomerEvent {
	//
	private int balance;
	private int customerId;
	
	
	/**
	 * 
	 */
	public CustomerEvent() {
	}
	
	public CustomerEvent(int customerId) {
		this.customerId = customerId;
	}
	
	/**
	 * 
	 */
	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	public int getCustomerId() {
		return customerId;
	}
	
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	/**
	 * 
	 */
	public static final CustomerEvent valueOf(int customerId, int balance) {
		CustomerEvent event = new CustomerEvent();
		event.setBalance(balance); event.setCustomerId(customerId); return event;
	}
}
