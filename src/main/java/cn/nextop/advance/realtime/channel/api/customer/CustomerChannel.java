package cn.nextop.advance.realtime.channel.api.customer;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.nextop.advance.realtime.XWebSocketChannel;
import cn.nextop.advance.realtime.XWebSocketSubscription;
import cn.nextop.advance.realtime.channel.api.AbstractApiChannel;
import cn.nextop.advance.realtime.channel.api.customer.entity.CustomerSubscription;
import cn.nextop.advance.realtime.channel.api.customer.event.CustomerEvent;
import cn.nextop.advance.realtime.glossary.Event;
import cn.nextop.advance.support.realtime.XWebSocketSession;

public class CustomerChannel extends AbstractApiChannel {
	//
	private static Logger LOGGER = LoggerFactory.getLogger (CustomerChannel.class);
	
	//
	public CustomerChannel () { super("customer", Event.CUSTOMER); }
	
	/**
	 * 
	 */
	@Override
	protected XWebSocketSubscription resolve(XWebSocketSession wss, Map<String, Object> args) {
		return CustomerSubscription.valueOf(1234567890);
	}
	
	@Override
	protected void subscribe(XWebSocketChannel wsc, SubscriberEx s, Map<String, Object> args) {
		final XWebSocketSession session = s.getSession();
		final CustomerSubscription x = s.getSubscription();
		final CustomerEvent z = CustomerEvent.valueOf(x.getCustomerId(), 12345);
		success(session, args, z); push(s, text(Event.CUSTOMER, z));
	}
}
