package com.viki.projects.saas_ai_editor.controller;

import com.viki.projects.saas_ai_editor.dto.subscription.PlanLimitResponse;
import com.viki.projects.saas_ai_editor.dto.subscription.UsageTodayResponse;
import com.viki.projects.saas_ai_editor.service.UsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/usage")
public class UsageController {

    private final UsageService usageService;

    @GetMapping("/today")
    public ResponseEntity<UsageTodayResponse> getTodayUsage() {
        Long userId = 1L; // TODO: get user id from auth context
        return ResponseEntity.ok(usageService.getTodayUsage(userId));
    }

    @GetMapping("/limits")
    public ResponseEntity<PlanLimitResponse> getPlanLimits() {
        Long userId = 1L; // TODO: get user id from auth context
        return ResponseEntity.ok(usageService.getCurrentSubscriptionLimits(userId));
    }

}
