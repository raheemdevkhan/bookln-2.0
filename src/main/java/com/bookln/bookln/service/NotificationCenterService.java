package com.bookln.bookln.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

@Service
public class NotificationCenterService {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss");
    private static final int MAX_NOTIFICATIONS = 100;

    private final AtomicLong idGenerator = new AtomicLong(0);
    private final List<NotificationMessage> notifications = new ArrayList<>();

    public synchronized NotificationMessage publish(String message) {
        NotificationMessage item = new NotificationMessage(
                idGenerator.incrementAndGet(),
                message,
                LocalDateTime.now().format(TIME_FORMATTER));

        notifications.add(item);
        if (notifications.size() > MAX_NOTIFICATIONS) {
            notifications.remove(0);
        }

        return item;
    }

    public synchronized List<NotificationMessage> getAfter(long lastSeenId) {
        List<NotificationMessage> fresh = new ArrayList<>();
        for (NotificationMessage notification : notifications) {
            if (notification.id() > lastSeenId) {
                fresh.add(notification);
            }
        }
        return fresh;
    }

    public record NotificationMessage(long id, String message, String timestamp) {
    }
}