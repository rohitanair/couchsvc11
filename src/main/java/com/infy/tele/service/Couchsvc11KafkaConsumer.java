package com.infy.tele.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class Couchsvc11KafkaConsumer {

    private final Logger log = LoggerFactory.getLogger(Couchsvc11KafkaConsumer.class);
    private static final String TOPIC = "topic_couchsvc11";

    @KafkaListener(topics = "topic_couchsvc11", groupId = "group_id")
    public void consume(String message) throws IOException {
        log.info("Consumed message in {} : {}", TOPIC, message);
    }
}
