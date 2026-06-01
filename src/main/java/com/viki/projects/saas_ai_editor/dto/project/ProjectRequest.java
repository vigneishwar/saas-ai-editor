package com.viki.projects.saas_ai_editor.dto.project;

import jakarta.validation.constraints.NotBlank;

public record ProjectRequest(
    @NotBlank String name
) {
}
