package com.viki.projects.saas_ai_editor.controller;


import com.viki.projects.saas_ai_editor.dto.subscription.*;
import com.viki.projects.saas_ai_editor.service.PlanService;
import com.viki.projects.saas_ai_editor.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BillingController {

    private final PlanService planService;
    private final SubscriptionService subscriptionService;

    @GetMapping("/api/plans")
    public ResponseEntity<List<PlanResponse>> getAvailablePlans() {
        return ResponseEntity.ok(planService.getAllPlans());
    }

    @GetMapping("/api/me/subscription")
    public ResponseEntity<SubscriptionResponse> getMySubscription() {
        Long userId = 1L; // TODO: get user id from auth context
        return ResponseEntity.ok(subscriptionService.getSubscriptionByUserId(userId));
    }

    @PostMapping("/api/stripe/checkout")
    public ResponseEntity<CheckoutResponse>createCheckoutResponse(@RequestBody CheckoutRequest request){
        Long userId = 1L; // TODO: get user id from auth context
        CheckoutResponse response = subscriptionService.createCheckoutSessionUrl(request, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/stripe/portal")
    public ResponseEntity<StripePortalResponse> openCustomerPortal(){
        Long userId = 1L;
        return ResponseEntity.ok(subscriptionService.openCustomerPortal(userId));
    }
}

