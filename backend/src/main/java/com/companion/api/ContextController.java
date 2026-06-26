package com.companion.api;

import com.companion.domain.context.ContextService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/context")
public class ContextController {

    private final ContextService contextService;

    public ContextController(ContextService contextService) {
        this.contextService = contextService;
    }

    @PostMapping
    public ResponseEntity<Void> update(@Valid @RequestBody UpdateContextRequest request) {
        contextService.updateSnapshot(
                request.userId(),
                request.activeApp(),
                request.activeBranch(),
                request.activeProject()
        );
        return ResponseEntity.noContent().build();
    }

    record UpdateContextRequest(
            @NotNull UUID userId,
            String activeApp,
            String activeBranch,
            String activeProject
    ) {}
}
