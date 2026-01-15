package com.viki.projects.saas_ai_editor.dto.auth;

public record AuthResponse(
    String token,
    UserProfileResponse user
) {

}
