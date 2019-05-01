package com.sam.activemq.consumer;

import com.sam.activemq.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Session;

/**
 * @author sumit
 */
@Component
public class OrderConsumer {

    private static Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    @Qualifier("topicListener")
    @JmsListener(destination = "OrderTopic", containerFactory = "topicListener")
    public void receiveTopicMessage(@Payload Order order, @Headers MessageHeaders headers, @SuppressWarnings("rawtypes") Message message,
                                    Session session) {

        LOGGER.info("received <" + order + ">");
    }

    @Qualifier("topicListener")
    @JmsListener(destination = "OrderTopic", containerFactory = "topicListener")
    public void receiveTopicMessage2(@Payload Order order, @Headers MessageHeaders headers, @SuppressWarnings("rawtypes") Message message,
                                     Session session) {

        LOGGER.info("received <" + order + ">");
    }

}
