package com.sam.activemq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Session;
import com.sam.activemq.config.ActiveMQConfig;
import com.sam.activemq.entity.Order;

/**
 * @author sumit
 *
 */
@Component
public class OrderConsumer {

	private static Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

	@JmsListener(destination = ActiveMQConfig.ORDER_TOPIC, containerFactory = "topicListenerFactory")
	public void receiveTopicMessage(@Payload Order order, @Headers MessageHeaders headers, Message message,
			Session session) {

		LOGGER.info("received <" + order + ">");
	}

	@JmsListener(destination = ActiveMQConfig.ORDER_TOPIC, containerFactory = "topicListenerFactory")
	public void receiveTopicMessage2(@Payload Order order, @Headers MessageHeaders headers, Message message,
			Session session) {

		LOGGER.info("received <" + order + ">");
	}

}
