package com.companion.domain.user.dto;

import com.companion.domain.user.User;
import com.companion.domain.user.UserProfile;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        UserProfile profile,
        boolean active,
        LocalDateTime createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getProfile(),
                user.isActive(),
                user.getCreatedAt()
        );
    }
}
