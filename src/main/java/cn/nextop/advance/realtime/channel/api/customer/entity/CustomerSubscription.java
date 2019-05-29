package cn.nextop.advance.realtime.channel.api.customer.entity;

import cn.nextop.advance.realtime.channel.AbstractSubscription;

/**
 * 
 * @author Jingqi Xu
 */
public class CustomerSubscription extends AbstractSubscription {
	//
	private final int customerId;
	public final int getCustomerId() { return this.customerId; }
	private CustomerSubscription(int id) { this.customerId = id; }
	
	/**
	 * 
	 */
	@Override
	public int hashCode() {
		return Integer.hashCode(customerId);
	}
	
	@Override
	public String toString() {
		return Integer.toString(customerId);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == this) return true; if(obj == null) return false;
		if(!(obj instanceof CustomerSubscription)) return false;
		return customerId == ((CustomerSubscription)obj).customerId;
	}
	
	/**
	 * 
	 */
	public static final CustomerSubscription valueOf(final int customerId) {
		CustomerSubscription r = new CustomerSubscription(customerId); return r;
	}
}
