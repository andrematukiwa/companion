package com.companion.domain.card.scheduler;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class DeviceScheduleState {

    private String currentCardId;
    private Instant currentCardExpiresAt = Instant.EPOCH;

    // cardId → last time it was displayed
    private final Map<String, Instant> lastDisplayedAt = new ConcurrentHashMap<>();

    boolean isCurrentCardExpired() {
        return Instant.now().isAfter(currentCardExpiresAt);
    }

    void setCurrentCard(String cardId, long durationSeconds) {
        this.currentCardId = cardId;
        this.currentCardExpiresAt = Instant.now().plusSeconds(durationSeconds);
        this.lastDisplayedAt.put(cardId, Instant.now());
    }

    Instant lastDisplayedAt(String cardId) {
        return lastDisplayedAt.getOrDefault(cardId, Instant.EPOCH);
    }

    String getCurrentCardId() {
        return currentCardId;
    }
}
