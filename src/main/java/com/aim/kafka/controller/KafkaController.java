package com.aim.kafka.controller;

import com.aim.kafka.producer.KafkaService;
import com.aim.kafka.producer.KafkaServiceImpl;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.PathParam;

import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
@RequestMapping("/kafka")
public class KafkaController {

    private static Logger LOGGER = LoggerFactory.getLogger(KafkaController.class);

    @Inject
    private KafkaService kafkaService;

    @RequestMapping(value = "/sendmessage/{kafkaTopic}", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity sendMessage(@RequestBody String data, @PathVariable("kafkaTopic") String kafkaTopic) {
      //send data to Kafka
        Preconditions.checkNotNull(data);
        Preconditions.checkNotNull(kafkaTopic);
        ResponseEntity responseEntity = null;
        try{
            kafkaService.send(kafkaTopic, data);
            responseEntity = new ResponseEntity(HttpStatus.ACCEPTED);
        } catch (BadRequestException bre){
            responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            responseEntity = new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return responseEntity;

    }
}
