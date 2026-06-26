package com.companion.domain.context;

import java.time.LocalDateTime;
import java.util.UUID;

public record Context(
        UUID userId,
        String activeApp,
        String activeBranch,
        String activeProject,
        int focusMinutes,
        int pomodorosToday,
        LocalDateTime capturedAt
) {
    public static Context empty(UUID userId) {
        return new Context(userId, null, null, null, 0, 0, LocalDateTime.now());
    }

    public boolean hasActiveProject() {
        return activeProject != null && !activeProject.isBlank();
    }

    public boolean isLunchTime() {
        int hour = capturedAt.getHour();
        return hour >= 12 && hour < 14;
    }

    public boolean hasApp(String appName) {
        return activeApp != null && activeApp.toLowerCase().contains(appName.toLowerCase());
    }
}
