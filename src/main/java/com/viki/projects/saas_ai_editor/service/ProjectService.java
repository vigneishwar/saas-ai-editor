package com.viki.projects.saas_ai_editor.service;

import com.viki.projects.saas_ai_editor.dto.project.ProjectRequest;
import com.viki.projects.saas_ai_editor.dto.project.ProjectResponse;
import com.viki.projects.saas_ai_editor.dto.project.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectSummaryResponse> getProjectsByUserId(Long userId);
    ProjectResponse getProjectById(Long id, Long userId);

    ProjectResponse createProject(ProjectRequest request, Long userId);

    ProjectResponse updateProject(Long id, ProjectRequest request, Long userId);

    void softDeleteProject(Long id, Long userId);
}
