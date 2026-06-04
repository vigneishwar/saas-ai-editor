package com.viki.projects.saas_ai_editor.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
    @Email @NotBlank String username,
    @NotBlank @Size(min = 3, max = 50) String password,
    @NotBlank String name
) {
}
