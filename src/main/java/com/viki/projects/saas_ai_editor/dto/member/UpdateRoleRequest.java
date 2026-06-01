package com.viki.projects.saas_ai_editor.dto.member;

import com.viki.projects.saas_ai_editor.enums.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateRoleRequest(@NotNull ProjectRole role) {
}
