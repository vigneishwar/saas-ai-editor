package com.viki.projects.saas_ai_editor.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public enum ProjectRole {
    EDITOR(Set.of(ProjectPermission.VIEW, ProjectPermission.EDIT, ProjectPermission.DELETE, ProjectPermission.VIEW_MEMBERS)),
    VIEWER(Set.of(ProjectPermission.VIEW, ProjectPermission.VIEW_MEMBERS)),
    OWNER(Set.of(ProjectPermission.VIEW, ProjectPermission.EDIT, ProjectPermission.DELETE, ProjectPermission.MANAGE_MEMBERS, ProjectPermission.VIEW_MEMBERS));

    private final Set<ProjectPermission> permissions;

}
