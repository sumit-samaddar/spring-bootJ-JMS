package com.sam.activemq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;


/**
 * @author sumit
 */
@Component
public class Sender {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

    @Autowired
    @Qualifier("queueTemplate")
    private JmsTemplate jmsTemplate;

    public void send(String destination, String message) {
        LOGGER.info("sending message='{}' to destination='{}'", message, destination);
        jmsTemplate.convertAndSend(destination, message);

    }
    
    /*public void send(String destination, Order order) {
        LOGGER.info("sending message='{}' to destination='{}'", order, destination);
        jmsTemplate.convertAndSend(destination, order);

    }*/
}
