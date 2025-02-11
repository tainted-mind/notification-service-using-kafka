package com.darshilmehta.springboot.controller;

import com.darshilmehta.springboot.entity.Notification;
import com.darshilmehta.springboot.helpers.NotificationFollowerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/event-stream")
public class EventController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
    private static final long TIMEOUT = 60_000L;
    private static final long EVENTS_NEEDED = 60;

    private final NotificationFollowerHelper notificationFollowerHelper;

    @Autowired
    public EventController(NotificationFollowerHelper notificationFollowerHelper) {
        this.notificationFollowerHelper = notificationFollowerHelper;
    }

    @GetMapping("/events")
    public SseEmitter streamEvents() {
        LOGGER.info("Fetching latest posts by creators");

        SseEmitter sseEmitter = new SseEmitter(TIMEOUT);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        executorService.scheduleAtFixedRate(() -> {
            try {
                Notification n = notificationFollowerHelper.getRandomNotification();
                sseEmitter.send(SseEmitter.event().data(n));
            } catch (IOException e) {
                sseEmitter.completeWithError(e);
                executorService.shutdown();
            }
        }, 0, TIMEOUT / EVENTS_NEEDED, TimeUnit.MILLISECONDS);

        sseEmitter.onCompletion(executorService::shutdown);

        return sseEmitter;
    }

}
