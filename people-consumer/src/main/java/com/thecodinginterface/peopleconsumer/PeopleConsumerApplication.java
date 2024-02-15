package com.thecodinginterface.peopleconsumer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thecodinginterface.peopleschemas.Person;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
public class PeopleConsumerApplication {
	final static Logger log = LoggerFactory.getLogger(PeopleConsumerApplication.class);

	ObjectMapper objectMapper;
	PeopleConsumerApplication(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@KafkaListener(topics = "${kafka-topics.people.name}", groupId = "${spring.application.name}")
	void listen(ConsumerRecord<String, JsonNode> consumerRecord) {
		var jsonNode = consumerRecord.value();

		try {
			var person = objectMapper.convertValue(jsonNode, Person.class);

			log.info("Consumed {}", person);
		} catch (Exception e) {
			log.error("Failed consuming person", e);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(PeopleConsumerApplication.class, args);
	}

}
