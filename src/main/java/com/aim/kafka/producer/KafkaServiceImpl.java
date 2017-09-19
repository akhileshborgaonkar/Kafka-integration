package com.aim.kafka.producer;

import com.aim.kafka.domain.PayloadBean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import java.io.IOException;

@Service
public class KafkaServiceImpl implements KafkaService {

    @Inject
    private Sender sender;

    private static Logger LOGGER = LoggerFactory.getLogger(KafkaServiceImpl.class);
    private ObjectMapper mapper = new ObjectMapper();

    private PayloadBean convertToPayloadObject(final String payload) {
        PayloadBean payloadBean = null;
        try {
            //payloadBean = mapper.readValue(data, PayloadBean.class);

            JsonNode rootNode = mapper.readTree(payload);
            String topic = mapper.writeValueAsString(rootNode.get("topic")).trim().replace("\"","");
            String data = mapper.writeValueAsString(rootNode.get("data"));
            payloadBean = new PayloadBean(topic, data);
            LOGGER.info("=== Converted payload to Object {} === ", payloadBean.getTopic().toString());

        } catch (IOException | NullPointerException ioe ) {
            LOGGER.info("=== Error Reading Payload ===");
            throw new BadRequestException();
        }

        return payloadBean;
    }

    @Override
    public void send(final String topic, final String data) throws BadRequestException{
       // PayloadBean payload = convertToPayloadObject(data);
       // Preconditions.checkNotNull(payload);

        sender.sendMessage(topic, data);

    }
}
