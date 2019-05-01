package com.sam.activemq.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.time.Instant;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQBlobMessage;
import org.apache.activemq.command.ActiveMQBytesMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.apache.commons.io.IOUtils;

import com.sam.activemq.file.Constants;
import com.sam.activemq.file.FileAsByteArrayManager;

/**
 * A message consumer which consumes the message from ActiveMQ Broker
 * 
 * @author Mary.Zheng
 *
 */
public class QueueMessageConsumer implements MessageListener {

	private String activeMqBrokerUri;
	private String username;
	private String password;
	private String destinationName;
	private FileAsByteArrayManager fileManager = new FileAsByteArrayManager();

	public QueueMessageConsumer(String activeMqBrokerUri, String username, String password) {
		super();
		this.activeMqBrokerUri = activeMqBrokerUri;
		this.username = username;
		this.password = password;
	}

	public void run() throws JMSException {
		ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(username, password, activeMqBrokerUri);
		Connection connection = factory.createConnection();
		connection.start();
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		Destination destination = session.createQueue(destinationName);

		MessageConsumer consumer = session.createConsumer(destination);
		consumer.setMessageListener(this);

		System.out.println(String.format("QueueMessageConsumer Waiting for messages at queue='%s' broker='%s'",
				destinationName, this.activeMqBrokerUri));
	}

	@Override
	public void onMessage(Message message) {

		try {
			String filename = message.getStringProperty(Constants.FILE_NAME);

			Instant start = Instant.now();

			if (message instanceof ActiveMQTextMessage) {
				handleTextMessage((ActiveMQTextMessage) message);
			} else if (message instanceof ActiveMQBlobMessage) {
				handleBlobMessage((ActiveMQBlobMessage) message, filename);
			} else if (message instanceof ActiveMQBytesMessage) {
				handleBytesMessage((ActiveMQBytesMessage) message, filename);
			} else {
				System.out.println("test");
			}

			Instant end = Instant.now();
			System.out
					.println("Consumed message with filename [" + filename + "], took " + Duration.between(start, end));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleBytesMessage(ActiveMQBytesMessage bytesMessage, String filename)
			throws IOException, JMSException {
		String outputfileName = Constants.FILE_OUTPUT_BYTE_DIRECTORY + filename;
		fileManager.writeFile(bytesMessage.getContent().getData(), outputfileName);
		System.out.println("Received ActiveMQBytesMessage message");
	}

	private void handleBlobMessage(ActiveMQBlobMessage blobMessage, String filename)
			throws FileNotFoundException, IOException, JMSException {
		// for 1mb or bigger message
		String outputfileName = Constants.FILE_OUTPUT_BLOB_DIRECTORY + filename;
		InputStream in = blobMessage.getInputStream();
		fileManager.writeFile(IOUtils.toByteArray(in), outputfileName);
		System.out.println("Received ActiveMQBlobMessage message");
	}

	private void handleTextMessage(ActiveMQTextMessage txtMessage) throws JMSException {
		String msg = String.format("Received ActiveMQTextMessage [ %s ]", txtMessage.getText());
		System.out.println(msg);
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
}
