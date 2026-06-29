package com.companion.domain.card.scheduler;

import com.companion.domain.card.Card;
import com.companion.domain.card.CardData;
import com.companion.domain.card.history.CardHistory;
import com.companion.domain.card.history.CardHistoryRepository;
import com.companion.domain.context.Context;
import com.companion.domain.context.ContextService;
import com.companion.domain.device.Device;
import com.companion.domain.device.DeviceRepository;
import com.companion.domain.device.DeviceType;
import com.companion.infrastructure.sse.SseEmitterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CardScheduler {

    private static final Logger log = LoggerFactory.getLogger(CardScheduler.class);

    private final List<Card> cards;
    private final SseEmitterManager emitterManager;
    private final DeviceRepository deviceRepository;
    private final ContextService contextService;
    private final CardHistoryRepository cardHistoryRepository;

    // deviceId → schedule state
    private final Map<UUID, DeviceScheduleState> states = new ConcurrentHashMap<>();

    public CardScheduler(List<Card> cards,
                         SseEmitterManager emitterManager,
                         DeviceRepository deviceRepository,
                         ContextService contextService,
                         CardHistoryRepository cardHistoryRepository) {
        this.cards = cards;
        this.emitterManager = emitterManager;
        this.deviceRepository = deviceRepository;
        this.contextService = contextService;
        this.cardHistoryRepository = cardHistoryRepository;
    }

    @Scheduled(fixedDelay = 5_000)
    public void tick() {
        deviceRepository.findAll().stream()
                .filter(d -> d.isActive() && d.getType() == DeviceType.KINDLE)
                .filter(d -> emitterManager.isConnected(d.getId()))
                .forEach(this::processDevice);
    }

    // Allow external events (e.g. build failure) to push a priority-0 card immediately
    public void pushAlert(UUID deviceId, CardData cardData) {
        if (!emitterManager.isConnected(deviceId)) return;

        DeviceScheduleState state = states.computeIfAbsent(deviceId, id -> new DeviceScheduleState());
        state.setCurrentCard(cardData.type(), cardData.durationSeconds());

        emitterManager.sendAlert(deviceId, cardData.toEvent());
        log.info("Alert pushed to device {}: {}", deviceId, cardData.type());
    }

    private void processDevice(Device device) {
        UUID deviceId = device.getId();
        DeviceScheduleState state = states.computeIfAbsent(deviceId, id -> new DeviceScheduleState());

        if (!state.isCurrentCardExpired()) return;

        Context context = contextService.buildContext(device.getUser().getId());

        List<Card> candidates = cards.stream()
                .filter(c -> c.canDisplay(context))
                .toList();

        if (candidates.isEmpty()) {
            log.debug("No cards available for device {}", deviceId);
            return;
        }

        Card next = pickNext(candidates, state);
        CardData cardData = next.generate(context);

        state.setCurrentCard(next.getId(), cardData.durationSeconds());

        String eventName = cardData.priority() == 0 ? "alert" : "card";
        emitterManager.send(deviceId, eventName, cardData.toEvent());

        saveHistory(device, next, cardData);
        log.debug("Sent {} to device {}", next.getId(), deviceId);
    }

    private Card pickNext(List<Card> candidates, DeviceScheduleState state) {
        // priority 0 always wins immediately
        return candidates.stream()
                .filter(c -> c.getPriority() == 0)
                .findFirst()
                .orElseGet(() -> candidates.stream()
                        .sorted(Comparator
                                .comparing((Card c) -> state.lastDisplayedAt(c.getId()))
                                .thenComparingInt(Card::getPriority))
                        .findFirst()
                        .orElseThrow());
    }

    private void saveHistory(Device device, Card card, CardData cardData) {
        try {
            cardHistoryRepository.save(
                    new CardHistory(device, card.getId(), cardData.durationSeconds() * 1000));
        } catch (Exception ex) {
            log.warn("Failed to save card history for device {}", device.getId(), ex);
        }
    }
}
