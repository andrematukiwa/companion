package com.companion.infrastructure.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SseEmitterManager {

    private static final Logger log = LoggerFactory.getLogger(SseEmitterManager.class);
    private static final long SSE_TIMEOUT_MS = 30 * 60 * 1000L; // 30 min

    // deviceId → emitter
    private final Map<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter connect(UUID deviceId) {
        // replace any stale emitter for this device
        SseEmitter existing = emitters.remove(deviceId);
        if (existing != null) {
            existing.complete();
        }

        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT_MS);

        emitter.onCompletion(() -> {
            emitters.remove(deviceId);
            log.info("SSE completed for device {}", deviceId);
        });
        emitter.onTimeout(() -> {
            emitters.remove(deviceId);
            log.info("SSE timed out for device {}", deviceId);
        });
        emitter.onError(ex -> {
            emitters.remove(deviceId);
            log.warn("SSE error for device {}: {}", deviceId, ex.getMessage());
        });

        emitters.put(deviceId, emitter);
        log.info("SSE connected for device {}", deviceId);
        return emitter;
    }

    public void send(UUID deviceId, String eventName, Object data) {
        SseEmitter emitter = emitters.get(deviceId);
        if (emitter == null) return;

        try {
            emitter.send(SseEmitter.event().name(eventName).data(data));
        } catch (IOException ex) {
            log.warn("Failed to send SSE to device {}, removing emitter", deviceId);
            emitters.remove(deviceId);
            emitter.completeWithError(ex);
        }
    }

    public void sendHeartbeat(UUID deviceId) {
        send(deviceId, "heartbeat", "{}");
    }

    public void sendCard(UUID deviceId, CardEvent event) {
        send(deviceId, "card", event);
    }

    public void sendAlert(UUID deviceId, CardEvent event) {
        send(deviceId, "alert", event);
    }

    public boolean isConnected(UUID deviceId) {
        return emitters.containsKey(deviceId);
    }

    public int connectedCount() {
        return emitters.size();
    }
}
