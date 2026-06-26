package com.companion.domain.context;

import com.companion.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "context_snapshots")
@Getter
@Setter
@NoArgsConstructor
public class ContextSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "captured_at", nullable = false)
    private LocalDateTime capturedAt = LocalDateTime.now();

    @Column(name = "active_app", length = 100)
    private String activeApp;

    @Column(name = "active_branch", length = 150)
    private String activeBranch;

    @Column(name = "active_project", length = 150)
    private String activeProject;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> payload = Map.of();

    public ContextSnapshot(User user, String activeApp, String activeBranch, String activeProject) {
        this.user = user;
        this.activeApp = activeApp;
        this.activeBranch = activeBranch;
        this.activeProject = activeProject;
    }
}
