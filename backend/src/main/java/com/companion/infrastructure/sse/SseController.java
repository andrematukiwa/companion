package com.companion.infrastructure.sse;

import com.companion.domain.device.DeviceService;
import com.companion.domain.device.DeviceToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("/api/sse")
public class SseController {

    private static final Logger log = LoggerFactory.getLogger(SseController.class);

    private final DeviceService deviceService;
    private final SseEmitterManager emitterManager;

    public SseController(DeviceService deviceService, SseEmitterManager emitterManager) {
        this.deviceService = deviceService;
        this.emitterManager = emitterManager;
    }

    @GetMapping(value = "/kindle", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@RequestParam String token) {
        DeviceToken deviceToken;
        try {
            deviceToken = deviceService.validateToken(token);
        } catch (SecurityException ex) {
            log.warn("Rejected SSE connection with invalid token");
            return ResponseEntity.status(401).build();
        }

        var deviceId = deviceToken.getDevice().getId();
        SseEmitter emitter = emitterManager.connect(deviceId);

        // send an immediate heartbeat so the client knows the connection is live
        try {
            emitter.send(SseEmitter.event().name("heartbeat").data("{}"));
        } catch (IOException ex) {
            log.warn("Failed to send initial heartbeat to device {}", deviceId);
        }

        return ResponseEntity.ok(emitter);
    }
}
