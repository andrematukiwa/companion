package com.companion.domain.device;

import com.companion.domain.device.dto.DeviceResponse;
import com.companion.domain.device.dto.RegisterDeviceRequest;
import com.companion.domain.device.dto.RegisterDeviceResponse;
import com.companion.domain.user.User;
import com.companion.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Service
public class DeviceService {

    private static final int TOKEN_BYTES = 48;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final DeviceRepository deviceRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final UserRepository userRepository;

    public DeviceService(DeviceRepository deviceRepository,
                         DeviceTokenRepository deviceTokenRepository,
                         UserRepository userRepository) {
        this.deviceRepository = deviceRepository;
        this.deviceTokenRepository = deviceTokenRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public RegisterDeviceResponse register(RegisterDeviceRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + request.userId()));

        Device device = new Device(user, request.name(), request.type());
        deviceRepository.save(device);

        String token = generateToken();
        deviceTokenRepository.save(new DeviceToken(device, token));

        return new RegisterDeviceResponse(device.getId(), device.getName(), device.getType(), token);
    }

    @Transactional(readOnly = true)
    public List<DeviceResponse> findByUser(UUID userId) {
        return deviceRepository.findByUserIdAndActiveTrue(userId).stream()
                .map(DeviceResponse::from)
                .toList();
    }

    @Transactional
    public void revokeToken(String token) {
        DeviceToken deviceToken = deviceTokenRepository.findActiveToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token not found or already revoked"));
        deviceToken.setRevoked(true);
    }

    @Transactional
    public void deactivate(UUID deviceId) {
        Device device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new IllegalArgumentException("Device not found: " + deviceId));
        device.setActive(false);
    }

    // Validates token and returns the DeviceToken — used by the SSE layer
    @Transactional(readOnly = true)
    public DeviceToken validateToken(String token) {
        return deviceTokenRepository.findActiveToken(token)
                .filter(dt -> dt.getDevice().getType() == DeviceType.KINDLE)
                .orElseThrow(() -> new SecurityException("Invalid or unauthorized token"));
    }

    private String generateToken() {
        byte[] bytes = new byte[TOKEN_BYTES];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
