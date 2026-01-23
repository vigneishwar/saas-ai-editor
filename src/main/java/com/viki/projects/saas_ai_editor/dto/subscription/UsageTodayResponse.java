package com.viki.projects.saas_ai_editor.dto.subscription;

public record UsageTodayResponse(
        Integer tokenUsed,
        Integer tokensLimit,
        Integer previewsRunning,
        Integer previewsLimit
) {
}
