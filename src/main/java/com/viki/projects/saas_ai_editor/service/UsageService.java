package com.viki.projects.saas_ai_editor.service;

import com.viki.projects.saas_ai_editor.dto.subscription.PlanLimitResponse;
import com.viki.projects.saas_ai_editor.dto.subscription.UsageTodayResponse;

public interface UsageService {
    UsageTodayResponse getTodayUsage(Long userId);

    PlanLimitResponse getCurrentSubscriptionLimits(Long userId);
}
