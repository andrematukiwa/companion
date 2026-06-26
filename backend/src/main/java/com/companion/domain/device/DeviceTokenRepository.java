package com.companion.domain.device;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, UUID> {

    @Query("""
        SELECT dt FROM DeviceToken dt
        JOIN FETCH dt.device d
        JOIN FETCH d.user
        WHERE dt.token = :token
          AND dt.revoked = false
          AND d.active = true
    """)
    Optional<DeviceToken> findActiveToken(String token);
}
