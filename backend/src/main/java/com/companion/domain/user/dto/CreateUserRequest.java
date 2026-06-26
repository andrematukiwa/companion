package com.companion.domain.user.dto;

import com.companion.domain.user.UserProfile;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotNull UserProfile profile
) {}
