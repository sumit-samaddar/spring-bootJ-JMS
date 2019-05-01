package com.sam.activemq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * @author sumit
 */
@Component
public class Receiver {

	private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

	private CountDownLatch latch = new CountDownLatch(1);

	public CountDownLatch getLatch() {
		return latch;
	}

	@Qualifier("queueListener")
	@JmsListener(destination = "MyQueue", containerFactory = "queueListener")
	public void receive(String message) {
		LOGGER.info("received message='{}'", message);
		latch.countDown();
	}
}
