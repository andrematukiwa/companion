package com.companion.domain.card.impl;

import com.companion.domain.card.Card;
import com.companion.domain.card.CardData;
import com.companion.domain.context.Context;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
public class DockerCard implements Card {

    @Override
    public String getId() { return "DockerCard"; }

    @Override
    public String getTitle() { return "Docker"; }

    @Override
    public int getPriority() { return 2; }

    @Override
    public Duration getDuration() { return Duration.ofSeconds(20); }

    @Override
    public boolean canDisplay(Context context) {
        return true; // always relevant for a developer
    }

    @Override
    public CardData generate(Context context) {
        List<Map<String, String>> services = fetchDockerStatus();

        boolean anyDown = services.stream()
                .anyMatch(s -> "offline".equals(s.get("status")));

        int priority = anyDown ? 0 : getPriority();

        return new CardData(getId(), getTitle(), priority,
                (int) getDuration().toSeconds(),
                Map.of("services", services));
    }

    private List<Map<String, String>> fetchDockerStatus() {
        // Phase 1: static placeholder — Phase 2 Windows Agent will push real data
        return List.of(
                Map.of("name", "API",      "status", "online"),
                Map.of("name", "Redis",    "status", "online"),
                Map.of("name", "Postgres", "status", "online"),
                Map.of("name", "Worker",   "status", "online")
        );
    }
}
