package com.companion.domain.device.dto;

import com.companion.domain.device.DeviceType;

import java.util.UUID;

public record RegisterDeviceResponse(
        UUID deviceId,
        String name,
        DeviceType type,
        String token
) {}
