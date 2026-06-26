package com.companion.infrastructure.sse;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CardEvent(
        String type,
        String title,
        int priority,
        int durationSeconds,
        Object content
) {
    public static CardEvent heartbeat() {
        return new CardEvent(null, null, 0, 0, null);
    }

    public static CardEvent clear() {
        return new CardEvent("clear", null, 0, 0, null);
    }
}
