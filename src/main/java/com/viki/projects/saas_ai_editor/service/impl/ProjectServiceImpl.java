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
        // one way to convert List<Project> to List<ProjectSummaryResponse> is to use stream and map each Project to ProjectSummaryResponse using the mapper
//        return projectRepository.findAllAccessibleByUser(userId)
//                .stream()
//                .map(projectMapper::toProjectSummaryResponse) // Convert each Project entity to a ProjectSummaryResponse DTO
//                .toList();

        // another way is to add a method in the mapper that takes a List<Project> and returns a List<ProjectSummaryResponse>
        return projectMapper.toProjectSummaryResponse(projectRepository.findAllAccessibleByUser(userId));
    }

    @Override
    public ProjectResponse getProjectById(Long id, Long userId) {
        Project project = getAccessibleProjectById(id, userId); // fetch the project with the given id and check if the user has access to it
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request, Long userId) {
        User owner = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Project project = Project.builder()
                .name(request.name()) // name is coming from request
                .owner(owner) // owner is the user creating the project
                .isPublic(false)
                .build();
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project); // this will convert the Project entity to a ProjectResponse DTO
    }

    @Override
    public ProjectResponse updateProject(Long id, ProjectRequest request, Long userId) {
        Project project = getAccessibleProjectById(id, userId); // fetch the project with the given id and check if the user has access to it

        if (!project.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Only the owner can update the project");
        }

        project.setName(request.name()); // update the name of the project with the name coming from request
        project = projectRepository.save(project); // save the updated project to the database
        return projectMapper.toProjectResponse(project); // convert the updated Project entity to a ProjectResponse DTO and return it
    }

    @Override
    public void softDeleteProject(Long id, Long userId) {
        Project project = getAccessibleProjectById(id, userId);

        if (!project.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Only the owner can delete the project");
        }
        project.setDeletedAt(java.time.Instant.now()); // set the deletedAt field to the current timestamp to mark the project as deleted
        projectRepository.save(project); // save the updated project to the database

    }

    // This is a helper method to fetch a project by id and check if the user has access to it.
    // If the project is not found or the user does not have access, it will throw an exception.
    private Project getAccessibleProjectById(Long id, Long userId) {
        return projectRepository.findAccessibleProjectById(id, userId)
                .orElseThrow(() -> new RuntimeException("Project not found or access denied"));
    }
}
