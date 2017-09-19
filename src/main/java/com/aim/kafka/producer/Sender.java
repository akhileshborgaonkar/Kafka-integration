package com.aim.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class Sender {

    @Value("${kafka.bootstrap.servers}")
    private String bootstrapServers;

    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);
    @Value("${zookeeper_server}")
    private String ZOOKEEPER_SERVER;
    @Value("${zookeeper_connection_timeout}")
    private int KAFKA_ZOOKEEPER_CONNECTION_TIMEOUT;
    @Value("${kafka_replication_factor}")
    private int DEFAULT_REPLICATION_FACTOR;
    @Value("${zookeeper_session_timeout}")
    private int KAFKA_ZOOKEEPER_SESSION_TIMEOUT;
    @Value("${kafka_default_num_partition}")
    private int DEFAULT_NUM_PARTITIONS;


    @Autowired
    private KafkaTemplate template;

    /**
     * Send message to given topic
     *
     * @param topic
     * @param message
     */
    public void sendMessage(String topic, String message) {
        LOGGER.info("=== Info ==== sending .... {}", message);
        ListenableFuture<SendResult<Integer, String>> future = template.send(topic, message);
        future.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                LOGGER.info("sent message='{}' with offset={}", message, result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                LOGGER.info("=== Error === unable to send message='{}'", message, ex);
            }

        });
    }
}