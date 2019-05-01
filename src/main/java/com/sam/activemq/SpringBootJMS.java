package com.sam.activemq;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sam.activemq.entity.Order;
import com.sam.activemq.producer.OrderSender;
import com.sam.activemq.producer.Sender;

@SpringBootApplication
public class SpringBootJMS implements CommandLineRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootJMS.class);

	@Autowired
	Sender sender;

	@Autowired
	private OrderSender orderSender;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootJMS.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		LOGGER.info("<<<<<<<<<<<<<<<<<<<P2P>>>>>>>>>>>>>>>>>>>>>");
		LOGGER.info(
				"Application started with command-line arguments: {} . \n To kill this application, press Ctrl + C.",
				Arrays.toString(args));

		sender.send("MyQueue", "Hello Boot!");

		LOGGER.info("<<<<<<<<<<<<<<<<<<<P2P>>>>>>>>>>>>>>>>>>>>>");

		LOGGER.info("<<<<<<<<<<<<<<<<<<<TOPIC>>>>>>>>>>>>>>>>>>>>>");

		LOGGER.info("Spring Boot ActiveMQ Publish Subscribe Topic Configuration Example");

		for (int i = 0; i < 5; i++) {
			Order order = new Order(Integer.valueOf(i), "me", "you", new BigDecimal(i), LocalDateTime.now());
			orderSender.sendTopic(order);
		}

		LOGGER.info("<<<<<<<<<<<<<<<<<<<TOPIC>>>>>>>>>>>>>>>>>>>>>");

		LOGGER.info("Waiting for all ActiveMQ JMS Messages to be consumed");
		TimeUnit.SECONDS.sleep(3);
		System.exit(-1);

	}
}
