package com.viki.projects.saas_ai_editor.entity;


import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProjectFile {

    private Long id;
    private Project project;
    private String path;
    private String minioObjectKey;
    private User createdBy;
    private User updatedBy;
    private Instant createdAt;
    private Instant updatedAt;
}
