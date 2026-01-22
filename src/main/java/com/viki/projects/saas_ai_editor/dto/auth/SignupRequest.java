package com.viki.projects.saas_ai_editor.dto.auth;

public record SignupRequest(
    String email,
    String password,
    String name
) {
}
