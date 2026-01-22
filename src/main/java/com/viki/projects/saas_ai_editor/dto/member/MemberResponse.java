package com.viki.projects.saas_ai_editor.dto.member;

import com.viki.projects.saas_ai_editor.enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long id,
        String email,
        String name,
        String avatarUrl,
        ProjectRole role,
        Instant invitedAt
) {
}
