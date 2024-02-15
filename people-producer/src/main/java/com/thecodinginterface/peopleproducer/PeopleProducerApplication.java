package com.thecodinginterface.peopleproducer;

import com.thecodinginterface.peopleschemas.Person;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import scala.Int;

import java.util.Map;
import java.util.concurrent.ExecutionException;

@SpringBootApplication
@RestController
public class PeopleProducerApplication {
	static final Logger log = LoggerFactory.getLogger(PeopleProducerApplication.class);

	@Value("${kafka-topics.people.name}")
	String peopleTopicName;

	@Value("${kafka-topics.people.partitions}")
	Integer peopleTopicPartitions;

	@Value("${kafka-topics.people.replicas}")
	Integer peopleTopicReplicas;

	KafkaTemplate<String, Person> kafkaTemplate;

	PeopleProducerApplication(KafkaTemplate<String, Person> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	@Bean
	NewTopic topic() {
		return TopicBuilder.name(peopleTopicName)
				.partitions(peopleTopicPartitions)
				.replicas(peopleTopicReplicas)
				.build();
	}

	@PostMapping("/api/people")
	Map<String, String> publish(@RequestBody Person person) throws ExecutionException, InterruptedException {

		var result = kafkaTemplate.send(peopleTopicName, person).get();

		var response = Map.of(
			"topic", result.getRecordMetadata().topic(),
			"offset", Long.toString(result.getRecordMetadata().offset()),
			"partition", Long.toString(result.getRecordMetadata().partition()),
			"byteSize", Integer.toString(result.getRecordMetadata().serializedValueSize())
		);

		return response;
	}

	public static void main(String[] args) {
		SpringApplication.run(PeopleProducerApplication.class, args);
	}

}
