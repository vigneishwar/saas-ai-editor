package com.viki.projects.saas_ai_editor.dto.member;

import com.viki.projects.saas_ai_editor.enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String username,
        String name,
        ProjectRole role,
        Instant invitedAt
) {
}
