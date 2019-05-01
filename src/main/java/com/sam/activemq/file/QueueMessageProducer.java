package com.sam.activemq.file;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.BlobMessage;

import com.sam.activemq.file.Constants;
import com.sam.activemq.file.FileAsByteArrayManager;

/**
 * A message producer which sends the file message to ActiveMQ Broker
 * 
 * @author Mary.Zheng
 *
 */
public class QueueMessageProducer {

	private String activeMqBrokerUri;
	private String username;
	private String password;

	private ActiveMQSession session;
	private MessageProducer msgProducer;
	private ConnectionFactory connFactory;
	private Connection connection;

	private FileAsByteArrayManager fileManager = new FileAsByteArrayManager();

	public QueueMessageProducer(String activeMqBrokerUri, String username, String password) {
		super();
		this.activeMqBrokerUri = activeMqBrokerUri;
		this.username = username;
		this.password = password;
	}

	private void setup() throws JMSException {
		connFactory = new ActiveMQConnectionFactory(username, password, activeMqBrokerUri);
		connection = connFactory.createConnection();
		connection.start();
		session = (ActiveMQSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	private void close() {
		try {
			if (msgProducer != null) {
				msgProducer.close();
			}
			if (session != null) {
				session.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (Throwable ignore) {
		}
	}

	public void sendBytesMessages(String queueName) throws JMSException, IOException {

		setup();

		msgProducer = session.createProducer(session.createQueue(queueName));

		File[] files = new File(Constants.FILE_INPUT_DIRECTORY).listFiles();
		for (File file : files) {
			if (file.isFile()) {
				sendFileAsBytesMessage(file);
			}
		}

		close();
	}

	public void sendBlobMessages(String queueName) throws JMSException {

		this.activeMqBrokerUri = activeMqBrokerUri + Constants.BLOB_FILESERVER;
		setup();

		msgProducer = session.createProducer(session.createQueue(queueName));

		File[] files = new File(Constants.FILE_INPUT_DIRECTORY).listFiles();
		for (File file : files) {
			if (file.isFile()) {
				sendFileAsBlobMessage(file);
			}
		}

		close();
	}

	private void sendFileAsBlobMessage(File file) throws JMSException {
		Instant start = Instant.now();
		BlobMessage blobMessage = session.createBlobMessage(file);
		blobMessage.setStringProperty(Constants.FILE_NAME, file.getName());
		msgProducer.send(blobMessage);
		Instant end = Instant.now();
		System.out.println("sendFileAsBlobMessage for [" + file.getName() + "], took " + Duration.between(start, end));
	}

	private void sendFileAsBytesMessage(File file) throws JMSException, IOException {
		Instant start = Instant.now();
		BytesMessage bytesMessage = session.createBytesMessage();
		bytesMessage.setStringProperty(Constants.FILE_NAME, file.getName());
		bytesMessage.writeBytes(fileManager.readfileAsBytes(file));
		msgProducer.send(bytesMessage);
		Instant end = Instant.now();
		System.out.println("sendFileAsBytesMessage for [" + file.getName() + "], took " + Duration.between(start, end));
	}
}
