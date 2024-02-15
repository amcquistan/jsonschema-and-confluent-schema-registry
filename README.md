# JSON Schema with Spring Kafka and Confluent Schema Registry

This is the sample project to accompany the similarly titled [blogpost on The Coding Interface](https://thecodinginterface.com/blog/json-schema-and-confluent-schema-registry/).

### Running Locally

Start the docker compose enabled containerized environment.

```
docker compose up -d
```

Start the producer.

```
cd people-producer
./gradlew bootRun
```

Start the consumer.

```
cd people-consumer
./gradlew bootRun
```

### Generating New Schema Version

Update the schema definition within the people-schemas/src/resources/json directory with compliant JSON Schema definitions. Then execute the gradle assemble task to generate a distributable jar file within the people-schemas/build/libs directory.