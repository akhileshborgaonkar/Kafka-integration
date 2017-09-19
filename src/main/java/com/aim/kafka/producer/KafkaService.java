package com.aim.kafka.producer;

public interface KafkaService {
    public void send(final String kafkaTopic, final String data);
}
