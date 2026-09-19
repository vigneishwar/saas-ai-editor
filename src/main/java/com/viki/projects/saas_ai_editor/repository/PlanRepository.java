package com.viki.projects.saas_ai_editor.repository;

import com.viki.projects.saas_ai_editor.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
}
