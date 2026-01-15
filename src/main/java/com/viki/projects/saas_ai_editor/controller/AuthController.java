package com.viki.projects.saas_ai_editor.controller;

import com.viki.projects.saas_ai_editor.dto.auth.AuthResponse;
import com.viki.projects.saas_ai_editor.dto.auth.SignupRequest;
import com.viki.projects.saas_ai_editor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public ResponseEntity<AuthResponse> signup(SignupRequest request) {
        AuthResponse response = authService.signup(request);
        return ResponseEntity.ok(response);
    }
}
