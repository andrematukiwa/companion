package com.companion.domain.session;

import com.companion.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "focus_sessions")
@Getter
@Setter
@NoArgsConstructor
public class FocusSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt = LocalDateTime.now();

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(length = 150)
    private String project;

    @Column(nullable = false)
    private int pomodoros = 0;

    public FocusSession(User user, String project) {
        this.user = user;
        this.project = project;
    }

    public boolean isActive() {
        return endedAt == null;
    }
}
