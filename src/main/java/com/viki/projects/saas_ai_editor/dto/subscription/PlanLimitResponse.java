package com.viki.projects.saas_ai_editor.dto.subscription;

public record PlanLimitResponse(
        String planName,
        Integer maxTokensPerDay,
        Integer maxProjects,
        Boolean unLimitedAi
) {
}
