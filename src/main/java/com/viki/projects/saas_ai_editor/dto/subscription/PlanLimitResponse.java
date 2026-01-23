package com.viki.projects.saas_ai_editor.dto.subscription;

public record PlanLimitResponse(
        String planName,
        int maxTokensPerDay,
        int maxProjects,
        boolean unLimitedAi
) {
}
