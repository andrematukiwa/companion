package com.companion.domain.card;

import com.companion.infrastructure.sse.CardEvent;

public record CardData(
        String type,
        String title,
        int priority,
        int durationSeconds,
        Object content
) {
    public CardEvent toEvent() {
        return new CardEvent(type, title, priority, durationSeconds, content);
    }
}
