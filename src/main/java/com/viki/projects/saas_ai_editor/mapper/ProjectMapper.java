package com.viki.projects.saas_ai_editor.mapper;


import com.viki.projects.saas_ai_editor.dto.project.ProjectResponse;
import com.viki.projects.saas_ai_editor.dto.project.ProjectSummaryResponse;
import com.viki.projects.saas_ai_editor.entity.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring") // This tells MapStruct to generate a Spring bean for this mapper
public interface ProjectMapper {

    ProjectResponse toProjectResponse(Project project);

    ProjectSummaryResponse toProjectSummaryResponse(Project project);

    List<ProjectSummaryResponse> toProjectSummaryResponse(List<Project> projects);

}
