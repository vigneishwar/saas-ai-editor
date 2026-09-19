package com.viki.projects.saas_ai_editor.service.impl;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.viki.projects.saas_ai_editor.dto.subscription.CheckoutRequest;
import com.viki.projects.saas_ai_editor.dto.subscription.CheckoutResponse;
import com.viki.projects.saas_ai_editor.dto.subscription.PortalResponse;
import com.viki.projects.saas_ai_editor.entity.Plan;
import com.viki.projects.saas_ai_editor.error.ResourceNotFoundException;
import com.viki.projects.saas_ai_editor.repository.PlanRepository;
import com.viki.projects.saas_ai_editor.security.AuthUtil;
import com.viki.projects.saas_ai_editor.service.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StripePaymentProcessorImpl implements PaymentProcessor {

    @Value("${client.url}")
    private String frontendUrl;

    private final AuthUtil authUtil;
    private final PlanRepository planRepository;


    @Override
    public CheckoutResponse createCheckoutSessionUrl(CheckoutRequest request) {

        Plan plan = planRepository.findById(request.planId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan" , request.planId().toString()));

        Long user = authUtil.getUserId();

        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(
                        SessionCreateParams.LineItem.builder().setPrice(plan.getStripePriceId()).setQuantity(1L).build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(frontendUrl + "/success.html?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(frontendUrl + "/cancel.html")
                .putMetadata("userId", user.toString())
                .putMetadata("planId", plan.getId().toString())
                .build();
        try {
            Session session = Session.create(params);
            return new CheckoutResponse(session.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public PortalResponse openCustomerPortal(Long userId) {
        return null;
    }
}
