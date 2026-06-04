package com.viki.projects.saas_ai_editor.dto.auth;

public record UserProfileResponse(
        Long id,
        String username,
        String name,
        String avatarUrl
) {
}
