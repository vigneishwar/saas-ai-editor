package com.viki.projects.saas_ai_editor.controller;

import com.viki.projects.saas_ai_editor.dto.auth.AuthResponse;
import com.viki.projects.saas_ai_editor.dto.auth.LoginRequest;
import com.viki.projects.saas_ai_editor.dto.auth.SignupRequest;
import com.viki.projects.saas_ai_editor.dto.auth.UserProfileResponse;
import com.viki.projects.saas_ai_editor.service.AuthService;
import com.viki.projects.saas_ai_editor.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
//@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE) // this will make all fields private and final by default. So need to specify private final for each field.
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request) {
        AuthResponse response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse>getProfile() {
        Long userId = 1L; // Placeholder for authenticated user ID
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

}
