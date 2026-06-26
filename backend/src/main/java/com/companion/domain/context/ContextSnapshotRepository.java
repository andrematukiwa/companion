package com.companion.domain.context;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ContextSnapshotRepository extends JpaRepository<ContextSnapshot, UUID> {

    Optional<ContextSnapshot> findTopByUserIdOrderByCapturedAtDesc(UUID userId);
}
