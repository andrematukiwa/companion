package com.companion.domain.card.history;

import com.companion.domain.device.Device;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "card_history")
@Getter
@NoArgsConstructor
public class CardHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Column(name = "card_type", nullable = false, length = 50)
    private String cardType;

    @Column(name = "displayed_at", nullable = false)
    private LocalDateTime displayedAt = LocalDateTime.now();

    @Column(name = "duration_ms", nullable = false)
    private int durationMs;

    public CardHistory(Device device, String cardType, int durationMs) {
        this.device = device;
        this.cardType = cardType;
        this.durationMs = durationMs;
    }
}
