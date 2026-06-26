package com.companion.api;

import com.companion.domain.device.DeviceService;
import com.companion.domain.device.dto.DeviceResponse;
import com.companion.domain.device.dto.RegisterDeviceRequest;
import com.companion.domain.device.dto.RegisterDeviceResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    public ResponseEntity<RegisterDeviceResponse> register(@Valid @RequestBody RegisterDeviceRequest request) {
        return ResponseEntity.status(201).body(deviceService.register(request));
    }

    @GetMapping
    public ResponseEntity<List<DeviceResponse>> findByUser(@RequestParam UUID userId) {
        return ResponseEntity.ok(deviceService.findByUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {
        deviceService.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/tokens/{token}/revoke")
    public ResponseEntity<Void> revokeToken(@PathVariable String token) {
        deviceService.revokeToken(token);
        return ResponseEntity.noContent().build();
    }
}
