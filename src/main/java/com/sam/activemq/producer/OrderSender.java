package com.sam.activemq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.sam.activemq.config.ActiveMQConfig;
import com.sam.activemq.entity.Order;

/**
 * @author sumit
 *
 */
@Service
public class OrderSender {

	private static Logger LOGGER = LoggerFactory.getLogger(OrderSender.class);

	@Autowired
	private JmsTemplate jmsTemplate;

	public void sendTopic(Order order) {
		LOGGER.info("sending with convertAndSend() to queue <" + order + ">");
		jmsTemplate.convertAndSend(ActiveMQConfig.ORDER_TOPIC, order);
	}

}
