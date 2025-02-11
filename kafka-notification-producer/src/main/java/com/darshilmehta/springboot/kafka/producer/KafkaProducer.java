package com.darshilmehta.springboot.kafka.producer;

import com.darshilmehta.springboot.event.handler.NotificationEventHandler;
import com.launchdarkly.eventsource.EventSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.concurrent.TimeUnit;

@Service
public class KafkaProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendNotification() throws InterruptedException {
        LOGGER.info("Started Kafka producer to receive notification events");

        String urlForNotificationEvents = "http://localhost:6060/api/v1/event-stream/events";
        String topicName = "notification_topic";

        NotificationEventHandler notificationEventHandler = new NotificationEventHandler(this.kafkaTemplate, topicName);

        EventSource.Builder eventSourceBuilder = new EventSource.Builder(notificationEventHandler, URI.create(urlForNotificationEvents));
        EventSource eventSource = eventSourceBuilder.build();
        eventSource.start();

        TimeUnit.MINUTES.sleep(10);
    }

}
