package com.viki.projects.saas_ai_editor.repository;

import com.viki.projects.saas_ai_editor.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(
            """
            SELECT p FROM Project p
            WHERE p.deletedAt IS NULL
                        and exists (
                            SELECT 1 FROM ProjectMember pm
                            WHERE pm.id.userId = :userId
                            and pm.id.projectId = p.id
                                    )
            ORDER BY p.updatedAt DESC
            """
    ) // Project is the entity name, not the table name. Always use the entity name in JPQL queries and not the table name.
    List<Project> findAllAccessibleByUser(@Param("userId") Long userId);

    @Query(
            """
            select p from Project p
            where p.id = :projectId
            and p.deletedAt IS NULL
            and exists (
                select 1 from ProjectMember pm
                where pm.id.userId = :userId
                and pm.id.projectId = :projectId
            )
            """
    ) // This query will fetch the project with the given projectId only if it is not deleted and the owner of the project
    // has the given userId. The left join fetch is used to fetch the owner of the project in the same query to avoid lazy
    // loading issues later when we try to access the owner of the project.
    Optional<Project> findAccessibleProjectById (@Param("projectId") Long projectId,
                                                 @Param("userId") Long userId);


}
