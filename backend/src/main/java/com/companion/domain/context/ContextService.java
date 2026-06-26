package com.companion.domain.context;

import com.companion.domain.session.FocusSessionRepository;
import com.companion.domain.user.User;
import com.companion.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class ContextService {

    private final ContextSnapshotRepository snapshotRepository;
    private final FocusSessionRepository sessionRepository;
    private final UserRepository userRepository;

    public ContextService(ContextSnapshotRepository snapshotRepository,
                          FocusSessionRepository sessionRepository,
                          UserRepository userRepository) {
        this.snapshotRepository = snapshotRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Context buildContext(UUID userId) {
        ContextSnapshot snapshot = snapshotRepository
                .findTopByUserIdOrderByCapturedAtDesc(userId)
                .orElse(null);

        int focusMinutes = sessionRepository.findActiveSession(userId)
                .map(s -> (int) ChronoUnit.MINUTES.between(s.getStartedAt(), LocalDateTime.now()))
                .orElse(0);

        int pomodorosToday = sessionRepository
                .sumPomodorosByUserAndDate(userId, LocalDate.now())
                .orElse(0);

        if (snapshot == null) {
            return new Context(userId, null, null, null, focusMinutes, pomodorosToday, LocalDateTime.now());
        }

        return new Context(
                userId,
                snapshot.getActiveApp(),
                snapshot.getActiveBranch(),
                snapshot.getActiveProject(),
                focusMinutes,
                pomodorosToday,
                snapshot.getCapturedAt()
        );
    }

    @Transactional
    public void updateSnapshot(UUID userId, String activeApp, String activeBranch, String activeProject) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        snapshotRepository.save(new ContextSnapshot(user, activeApp, activeBranch, activeProject));
    }
}
