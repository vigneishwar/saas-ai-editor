package com.viki.projects.saas_ai_editor.repository;

import com.viki.projects.saas_ai_editor.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(
            """
            SELECT p FROM Project p
            WHERE p.deletedAt IS NULL
            AND p.owner.id = :userId
            ORDER BY p.updatedAt DESC
            """
    ) // Project is the entity name, not the table name. Always use the entity name in JPQL queries and not the table name.
    List<Project> findAllAccessibleByUser(@Param("userId") Long userId);
}
