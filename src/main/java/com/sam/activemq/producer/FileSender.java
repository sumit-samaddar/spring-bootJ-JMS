package com.sam.activemq.producer;

import com.sam.activemq.file.Constants;
import com.sam.activemq.file.FileAsByteArrayManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Service
public class FileSender {

    private static Logger LOGGER = LoggerFactory.getLogger(FileSender.class);

    private FileAsByteArrayManager fileManager = new FileAsByteArrayManager();

    @Autowired
    @Qualifier("fileQueueTemplate")
    private JmsTemplate jmsTemplate;

    public void sendFile(File file) {
        LOGGER.info("sending file - " + file.getName() + "");

        Instant start = Instant.now();

        jmsTemplate.send(new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
                org.apache.activemq.ActiveMQSession activeMQSession = ((org.apache.activemq.ActiveMQSession) session);

                BytesMessage bytesMessage = activeMQSession.createBytesMessage();
                bytesMessage.setStringProperty(Constants.FILE_NAME, file.getName());
                try {
                    bytesMessage.writeBytes(fileManager.readfileAsBytes(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LOGGER.debug("created a byte message");
                return bytesMessage;
            }
        });

        Instant end = Instant.now();
        System.out.println("sendFileAsBytesMessage for [" + file.getName() + "], took " + Duration.between(start, end));


    }

}
