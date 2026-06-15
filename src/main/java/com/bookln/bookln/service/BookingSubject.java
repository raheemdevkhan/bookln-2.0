package com.bookln.bookln.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Observer Interface
interface Observer {
    void update(String message);
}

// Concrete Observers
class AdminObserver implements Observer {
    @Override
    public void update(String message) {
        System.out.println("ADMIN DASHBOARD ALERT: " + message);
    }
}

class MaintenanceObserver implements Observer {
    @Override
    public void update(String message) {
        System.out.println("MAINTENANCE STAFF ALERT: " + message);
    }
}

// Subject Class
@Service
public class BookingSubject {
    private final List<Observer> observers = new ArrayList<>();
    private final NotificationCenterService notificationCenterService;

    @Autowired
    public BookingSubject(NotificationCenterService notificationCenterService) {
        this.notificationCenterService = notificationCenterService;
        // Automatically attach observers when created
        attach(new AdminObserver());
        attach(new MaintenanceObserver());
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void notifyAllObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }

        notificationCenterService.publish(message);
    }

    public List<NotificationCenterService.NotificationMessage> getNotificationsAfter(long lastSeenId) {
        return notificationCenterService.getAfter(lastSeenId);
    }
}
