package com.viki.projects.saas_ai_editor.repository;

import com.viki.projects.saas_ai_editor.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
