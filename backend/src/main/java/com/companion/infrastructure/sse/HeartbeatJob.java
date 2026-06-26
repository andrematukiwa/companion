package com.companion.infrastructure.sse;

import com.companion.domain.device.DeviceRepository;
import com.companion.domain.device.DeviceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HeartbeatJob {

    private static final Logger log = LoggerFactory.getLogger(HeartbeatJob.class);

    private final SseEmitterManager emitterManager;
    private final DeviceRepository deviceRepository;

    public HeartbeatJob(SseEmitterManager emitterManager, DeviceRepository deviceRepository) {
        this.emitterManager = emitterManager;
        this.deviceRepository = deviceRepository;
    }

    @Scheduled(fixedDelay = 25_000)
    public void sendHeartbeats() {
        // send heartbeat only to Kindle devices that are currently connected
        deviceRepository.findAll().stream()
                .filter(d -> d.isActive() && d.getType() == DeviceType.KINDLE)
                .filter(d -> emitterManager.isConnected(d.getId()))
                .forEach(d -> {
                    emitterManager.sendHeartbeat(d.getId());
                    log.debug("Heartbeat sent to device {}", d.getId());
                });
    }
}
