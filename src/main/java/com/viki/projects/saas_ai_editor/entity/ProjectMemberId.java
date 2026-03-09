package com.viki.projects.saas_ai_editor.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable // we must annotate the class with @Embeddable to indicate that it is a composite key class that can be embedded in an entity. This allows us to use this class as the primary key for the ProjectMember entity.
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberId {
    private Long projectId;
    private Long userId;
}
