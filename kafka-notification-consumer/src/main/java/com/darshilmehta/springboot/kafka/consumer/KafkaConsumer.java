package com.darshilmehta.springboot.kafka.consumer;

import com.darshilmehta.springboot.entity.Follower;
import com.darshilmehta.springboot.entity.Notification;
import com.darshilmehta.springboot.helpers.NotificationFollowerHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class KafkaConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    private final String logFilePath = "C:\\Users\\darshil.mehta\\IdeaProjects\\notification-service\\kafka-notification-consumer\\src\\main\\resources\\logs.txt";

    private final NotificationFollowerHelper notificationFollowerHelper;
    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaConsumer(NotificationFollowerHelper notificationFollowerHelper, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.notificationFollowerHelper = notificationFollowerHelper;
    }

    @PostConstruct
    public void clearPreviousLogs() {
        try {
            Files.write(Paths.get(logFilePath), "".getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @KafkaListener(
            topics = "notification_topic",
            groupId = "notification-queue"
    )
    public void consume(String message) throws JsonProcessingException {
        LOGGER.info("Message received at consumer: {}", message);

        Notification notification = objectMapper.readValue(message, Notification.class);
        List<Follower> followerList = notificationFollowerHelper.getFollowersForACreator();

        for (Follower follower : followerList) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
                if (follower.isNotificationEnabled()) {
                    String notificationMessage = String.format("Hello %s, you have received a new notification from %s: %s", follower.getUsername(), notification.getCreatorName(), notification.getMessage());
                    writer.write(notificationMessage);
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
