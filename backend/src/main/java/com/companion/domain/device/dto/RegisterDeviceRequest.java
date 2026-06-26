package com.companion.domain.device.dto;

import com.companion.domain.device.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record RegisterDeviceRequest(
        @NotNull UUID userId,
        @NotBlank String name,
        @NotNull DeviceType type
) {}
