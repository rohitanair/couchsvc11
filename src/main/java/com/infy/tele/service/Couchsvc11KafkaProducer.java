package com.infy.tele.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Couchsvc11KafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(Couchsvc11KafkaProducer.class);
    private static final String TOPIC = "topic_couchsvc11";

    private KafkaTemplate<String, String> kafkaTemplate;

    public Couchsvc11KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        log.info("Producing message to {} : {}", TOPIC, message);
        this.kafkaTemplate.send(TOPIC, message);
    }
}
