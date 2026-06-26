package com.companion.domain.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface FocusSessionRepository extends JpaRepository<FocusSession, UUID> {

    @Query("SELECT s FROM FocusSession s WHERE s.user.id = :userId AND s.endedAt IS NULL ORDER BY s.startedAt DESC LIMIT 1")
    Optional<FocusSession> findActiveSession(UUID userId);

    @Query("SELECT COALESCE(SUM(s.pomodoros), 0) FROM FocusSession s WHERE s.user.id = :userId AND CAST(s.startedAt AS date) = :date")
    Optional<Integer> sumPomodorosByUserAndDate(UUID userId, LocalDate date);
}
