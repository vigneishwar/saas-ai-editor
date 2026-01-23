package com.viki.projects.saas_ai_editor.dto.subscription;

public record PlanResponse(
         Long id,
         String name,
         Integer maxProjects,
         Integer maxTokensPerDay,
         Boolean unLimitedAi,
         String price
) {
}
