package com.companion.domain.device.dto;

import com.companion.domain.device.Device;
import com.companion.domain.device.DeviceType;

import java.time.LocalDateTime;
import java.util.UUID;

public record DeviceResponse(
        UUID id,
        UUID userId,
        String name,
        DeviceType type,
        boolean active,
        LocalDateTime createdAt
) {
    public static DeviceResponse from(Device device) {
        return new DeviceResponse(
                device.getId(),
                device.getUser().getId(),
                device.getName(),
                device.getType(),
                device.isActive(),
                device.getCreatedAt()
        );
    }
}
