package com.companion.domain.card.impl;

import com.companion.domain.card.Card;
import com.companion.domain.card.CardData;
import com.companion.domain.context.Context;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;

@Component
public class SessionCard implements Card {

    @Override
    public String getId() { return "SessionCard"; }

    @Override
    public String getTitle() { return "Sessão"; }

    @Override
    public int getPriority() { return 2; }

    @Override
    public Duration getDuration() { return Duration.ofSeconds(20); }

    @Override
    public boolean canDisplay(Context context) {
        return context.focusMinutes() > 0 || context.pomodorosToday() > 0;
    }

    @Override
    public CardData generate(Context context) {
        return new CardData(getId(), getTitle(), getPriority(),
                (int) getDuration().toSeconds(),
                Map.of(
                        "focusMinutes",   context.focusMinutes(),
                        "pomodorosToday", context.pomodorosToday(),
                        "project",        context.activeProject() != null ? context.activeProject() : ""
                ));
    }
}
