# Helidon messaging with JMS
Example of messaging with RabbitMQ

Start RabbitMQ server with JMS plugin locally: 
```bash
docker run -d --network host --name test_rabbitmq rabbitmq:3-management-alpine
docker exec -it test_rabbitmq rabbitmq-plugins enable rabbitmq_jms_topic_exchange
```

Clone, build and run example:
```shell
git clone https://github.com/danielkec/helidon-jms-example.git
cd helidon-jms-example
mvn clean install
java -jar ./target/jms-example.jar
```

Open http://localhost:7001 and click `Send` button