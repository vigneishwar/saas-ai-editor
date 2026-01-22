package com.viki.projects.saas_ai_editor.service;

import com.viki.projects.saas_ai_editor.dto.subscription.CheckoutRequest;
import com.viki.projects.saas_ai_editor.dto.subscription.CheckoutResponse;
import com.viki.projects.saas_ai_editor.dto.subscription.StripePortalResponse;
import com.viki.projects.saas_ai_editor.dto.subscription.SubscriptionResponse;

public interface SubscriptionService {
    SubscriptionResponse getSubscriptionByUserId(Long userId);

    CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request, Long userId);

    StripePortalResponse openCustomerPortal(Long userId);
}
