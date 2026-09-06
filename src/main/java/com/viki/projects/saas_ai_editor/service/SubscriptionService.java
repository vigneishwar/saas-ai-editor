package com.viki.projects.saas_ai_editor.service;

import com.viki.projects.saas_ai_editor.dto.subscription.CheckoutRequest;
import com.viki.projects.saas_ai_editor.dto.subscription.CheckoutResponse;
import com.viki.projects.saas_ai_editor.dto.subscription.PortalResponse;
import com.viki.projects.saas_ai_editor.dto.subscription.SubscriptionResponse;

public interface SubscriptionService {
    SubscriptionResponse getSubscriptionByUserId(Long userId);

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request);

    PortalResponse openCustomerPortal(Long userId);
}
