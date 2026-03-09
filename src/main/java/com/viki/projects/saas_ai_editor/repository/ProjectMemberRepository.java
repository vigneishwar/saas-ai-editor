package com.viki.projects.saas_ai_editor.repository;


import com.viki.projects.saas_ai_editor.entity.ProjectMember;
import com.viki.projects.saas_ai_editor.entity.ProjectMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    List<ProjectMember> findByIdProjectId(Long projectId);
}
