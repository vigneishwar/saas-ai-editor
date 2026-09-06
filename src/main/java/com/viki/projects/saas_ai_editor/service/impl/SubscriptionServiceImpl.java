package com.viki.projects.saas_ai_editor.service.impl;

import com.viki.projects.saas_ai_editor.dto.subscription.CheckoutRequest;
import com.viki.projects.saas_ai_editor.dto.subscription.CheckoutResponse;
import com.viki.projects.saas_ai_editor.dto.subscription.PortalResponse;
import com.viki.projects.saas_ai_editor.dto.subscription.SubscriptionResponse;
import com.viki.projects.saas_ai_editor.service.SubscriptionService;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    @Override
    public SubscriptionResponse getSubscriptionByUserId(Long userId) {
        return null;
    }

    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request) {
        return null;
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }
}
