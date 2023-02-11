package com.onlinemed.servises.impl;

import com.onlinemed.model.dto.NotificationDto;

public interface NotificationReceiver {
    void notificationToPerson(NotificationDto notificationDto);

}
