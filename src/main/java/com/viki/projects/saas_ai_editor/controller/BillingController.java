package com.viki.projects.saas_ai_editor.controller;


import com.viki.projects.saas_ai_editor.dto.subscription.PlanResponse;
import com.viki.projects.saas_ai_editor.dto.subscription.SubscriptionResponse;
import com.viki.projects.saas_ai_editor.service.PlanService;
import com.viki.projects.saas_ai_editor.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}

