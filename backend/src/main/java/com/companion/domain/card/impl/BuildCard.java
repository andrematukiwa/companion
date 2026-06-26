package com.companion.domain.card.impl;

import com.companion.domain.card.Card;
import com.companion.domain.card.CardData;
import com.companion.domain.context.Context;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class BuildCard implements Card {

    public enum BuildStatus { SUCCESS, RUNNING, FAILED, UNKNOWN }

    public record BuildState(BuildStatus status, String step, String project, long durationMs) {
        public static BuildState unknown() {
            return new BuildState(BuildStatus.UNKNOWN, null, null, 0);
        }
    }

    // Updated externally by a webhook or Windows Agent in Phase 2
    private final AtomicReference<BuildState> lastBuild = new AtomicReference<>(BuildState.unknown());

    @Override
    public String getId() { return "BuildCard"; }

    @Override
    public String getTitle() { return "Build"; }

    @Override
    public int getPriority() {
        BuildStatus status = lastBuild.get().status();
        return status == BuildStatus.FAILED ? 0 : 2;
    }

    @Override
    public Duration getDuration() {
        return lastBuild.get().status() == BuildStatus.FAILED
                ? Duration.ofSeconds(60)
                : Duration.ofSeconds(25);
    }

    @Override
    public boolean canDisplay(Context context) {
        return lastBuild.get().status() != BuildStatus.UNKNOWN;
    }

    @Override
    public CardData generate(Context context) {
        BuildState state = lastBuild.get();
        return new CardData(getId(), getTitle(), getPriority(),
                (int) getDuration().toSeconds(),
                Map.of(
                        "status",     state.status().name(),
                        "step",       state.step() != null ? state.step() : "",
                        "project",    state.project() != null ? state.project() : "",
                        "durationMs", state.durationMs()
                ));
    }

    public void updateBuild(BuildState state) {
        lastBuild.set(state);
    }
}
