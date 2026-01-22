package com.viki.projects.saas_ai_editor.dto.project;

import com.viki.projects.saas_ai_editor.dto.auth.UserProfileResponse;

import java.time.Instant;

public record ProjectResponse(
        Long id,
        String name,
        Instant createdAt,
        Instant updatedAt,
        UserProfileResponse owner
) {
}
