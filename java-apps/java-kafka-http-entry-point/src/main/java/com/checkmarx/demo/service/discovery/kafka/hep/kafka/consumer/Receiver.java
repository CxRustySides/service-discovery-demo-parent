package com.checkmarx.demo.service.discovery.kafka.hep.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Slf4j
public class Receiver {

    private final BlockingQueue<String> receivedMessagesQueue = new ArrayBlockingQueue<>(1024);

    @KafkaListener(topics = "#{relatedServicesProperties.getKafkaProducerTopic()}")
    public void receiveMessage(Message<?> message) {
        log.info("Received Kafka message " + message);
        Object payload = message.getPayload();
        String value = payload.toString();
        receivedMessagesQueue.add(value);
    }

    @KafkaListener(topics = "#{relatedServicesProperties.getKafkaConsumerLoopTopic()}")
    public void receiveLoopedMessage(Message<?> message) {
        Random rand = new Random();
        String payload = (String) message.getPayload();
        String output = payload + " " + rand.nextInt();
        log.info("Received looped Kafka message = '{}'", output);
    }

    public BlockingQueue<String> getReceivedMessagesQueue() {
        return receivedMessagesQueue;
    }
}