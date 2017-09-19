package com.aim.kafka.producer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class Producer {
    private static final Logger log = LoggerFactory.getLogger(KafkaService.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String TOPIC = "clickstream";
    final static org.apache.kafka.clients.producer.Producer<String, String> producer;

    static {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.25.158:9091, 192.168.25.158:9092, 192.168.25.158:9093");
        props.put("client.id", "clickStreamProducer");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<>(props);
    }

    //to json
    public void sendToKafka(HttpServletRequest request, String payload) {

        Principal principal = request.getUserPrincipal();
        Map<String, String> map = new HashMap<>();
        String json = null;
        String user = null;
        try {
            mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            map = mapper.readValue(payload, new TypeReference<HashMap<String, String>>() {
            });
            user = principal != null ? principal.getName() : request.getRemoteUser();
            map.put("Timestamp", Instant.now().toString());
            map.put("Ip", request.getRemoteAddr());
            map.put("user", user != null ? user : "Unknown User");

            json = mapper.writeValueAsString(map);
        } catch (Exception e) {
            log.warn("=== ERROR during PArsing ====");
        }
        send(json, TOPIC);
        log.info(json + " request: " + user);
    }

    public void send(String topic, String message) {

        ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, message);
        producer.send(data);
    }

    public static void main(String[] args) {
        Producer p = new Producer();
        p.send("producttrend", "{\"PRODUCT_DESC\":\"This cake is as contemporary.\",\"PRICE\":\"350.00\"," +
                "\"SENT_AT\":\"2016-12-15T21:17:19.317Z\",\"PRODUCT_ID\":\"31\",\"NAME\":\"Aloha\"}");
    }
}
