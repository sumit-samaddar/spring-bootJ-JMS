package com.sam.activemq.producer;

import com.sam.activemq.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

/**
 * @author sumit
 */
@Service
public class OrderSender {

    private static Logger LOGGER = LoggerFactory.getLogger(OrderSender.class);

    @Autowired
    @Qualifier("topicTemplate")
    private JmsTemplate jmsTemplate;

    public void sendTopic(Order order) {
        LOGGER.info("sending with convertAndSend() to Topic <" + order + ">");
        jmsTemplate.convertAndSend("OrderTopic", order);
    }

}
