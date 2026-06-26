package com.companion.domain.card;

import com.companion.domain.context.Context;

import java.time.Duration;

public interface Card {

    String getId();

    String getTitle();

    int getPriority();

    Duration getDuration();

    boolean canDisplay(Context context);

    CardData generate(Context context);
}
