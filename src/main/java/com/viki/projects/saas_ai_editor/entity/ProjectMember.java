package com.viki.projects.saas_ai_editor.entity;

import com.viki.projects.saas_ai_editor.enums.ProjectRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "project_members")
public class ProjectMember {
    @EmbeddedId // what is EmbeddedId? It is used to specify a composite primary key class that is embedded in the entity.
    // In this case, ProjectMemberId is a composite key that consists of projectId and userId.
    private ProjectMemberId id;
    @ManyToOne
    @MapsId("projectId") // what is MapsId? It is used to specify that the project field is mapped to the projectId field
    // in the ProjectMemberId class. This allows us to use the project field as part of the composite key.
    private Project project;

    @ManyToOne
    @MapsId("userId") // what is MapsId? It is used to specify that the user field is mapped to the userId field in
    // the ProjectMemberId class. This allows us to use the user field as part of the composite key.
    private User user;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectRole role;
    private Instant invitedAt;
    private Instant acceptedAt;
}
