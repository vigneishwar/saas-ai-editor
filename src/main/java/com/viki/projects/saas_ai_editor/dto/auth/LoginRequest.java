package com.viki.projects.saas_ai_editor.dto.auth;

public record LoginRequest(
    String email,
    String password
) {
}
