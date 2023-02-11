package com.onlinemed.model.dto;

import java.util.UUID;

public record NotificationDto(UUID senderId, UUID receiverId, String name, String surname) {
}
