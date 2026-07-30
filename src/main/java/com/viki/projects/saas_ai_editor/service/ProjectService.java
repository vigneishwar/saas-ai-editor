package com.viki.projects.saas_ai_editor.service;

import com.viki.projects.saas_ai_editor.dto.project.ProjectRequest;
import com.viki.projects.saas_ai_editor.dto.project.ProjectResponse;
import com.viki.projects.saas_ai_editor.dto.project.ProjectSummaryResponse;

import java.util.List;

public interface ProjectService {
    List<ProjectSummaryResponse> getProjectsByUserId();
    ProjectResponse getProjectById(Long id);

    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse updateProject(Long id, ProjectRequest request);

    void softDeleteProject(Long id);
}
