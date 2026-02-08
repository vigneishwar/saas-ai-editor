package com.viki.projects.saas_ai_editor.service.impl;

import com.viki.projects.saas_ai_editor.dto.project.ProjectRequest;
import com.viki.projects.saas_ai_editor.dto.project.ProjectResponse;
import com.viki.projects.saas_ai_editor.dto.project.ProjectSummaryResponse;
import com.viki.projects.saas_ai_editor.entity.Project;
import com.viki.projects.saas_ai_editor.entity.User;
import com.viki.projects.saas_ai_editor.mapper.ProjectMapper;
import com.viki.projects.saas_ai_editor.repository.ProjectRepository;
import com.viki.projects.saas_ai_editor.repository.UserRepository;
import com.viki.projects.saas_ai_editor.service.ProjectService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;
    UserRepository userRepository;
    ProjectMapper projectMapper;

    @Override
    public List<ProjectSummaryResponse> getProjectsByUserId(Long userId) {
        return null;
    }

    @Override
    public ProjectResponse getProjectById(Long id, Long userId) {
        return null;
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Project project = Project.builder()
                .name(request.name()) // name is coming from request
                .owner(owner) // owner is the user creating the project
                .build();
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project); // this will convert the Project entity to a ProjectResponse DTO
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        return null;
    }

    @Override
    public void softDeleteProject(Long id, Long userId) {

    }
}
