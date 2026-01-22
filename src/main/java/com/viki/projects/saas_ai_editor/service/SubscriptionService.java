package com.viki.projects.saas_ai_editor.service;

import com.viki.projects.saas_ai_editor.dto.subscription.SubscriptionResponse;

public interface SubscriptionService {
    SubscriptionResponse getSubscriptionByUserId(Long userId);
}
