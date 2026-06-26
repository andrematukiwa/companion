package com.companion.domain.card.history;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface CardHistoryRepository extends JpaRepository<CardHistory, UUID> {

    @Query("SELECT MAX(h.displayedAt) FROM CardHistory h WHERE h.device.id = :deviceId AND h.cardType = :cardType")
    Optional<LocalDateTime> findLastDisplayedAt(UUID deviceId, String cardType);
}
