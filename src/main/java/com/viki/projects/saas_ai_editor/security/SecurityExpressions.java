package com.viki.projects.saas_ai_editor.security;

import com.viki.projects.saas_ai_editor.enums.ProjectPermission;
import com.viki.projects.saas_ai_editor.enums.ProjectRole;
import com.viki.projects.saas_ai_editor.repository.ProjectMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityExpressions {

    private final AuthUtil authUtil;
    private final ProjectMemberRepository projectMemberRepository;

    private boolean hasPermissions(Long projectId, ProjectPermission permission) {
        Long userId = authUtil.getUserId();
            return projectMemberRepository.findRoleByProjectIdAndUserId(projectId, userId)
                .map(role -> role.getPermissions().contains(permission))
                .orElse(false);
    }

    public boolean canViewProject(Long projectId) {
        // Implement your logic to check if the user can view the project with the given projectId
        return hasPermissions(projectId, ProjectPermission.VIEW);
    }

    public boolean canEditProject(Long projectId) {
        return hasPermissions(projectId, ProjectPermission.EDIT);
    }

    public boolean canDeleteProject(Long projectId) {
        return hasPermissions(projectId, ProjectPermission.DELETE);
    }
}
