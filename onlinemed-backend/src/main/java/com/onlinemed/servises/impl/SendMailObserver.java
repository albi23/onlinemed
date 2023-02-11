package com.onlinemed.servises.impl;

import com.onlinemed.model.dto.NotificationDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SendMailObserver {
    private final List<NotificationReceiver> observers = new ArrayList<>();

    public void addObserver(NotificationReceiver newObserver) {
        this.observers.add(newObserver);
    }

    public void notifyObservers(NotificationDto notificationDto) {
        for (NotificationReceiver observer : observers) {
            observer.notificationToPerson(notificationDto);
        }
    }
}
