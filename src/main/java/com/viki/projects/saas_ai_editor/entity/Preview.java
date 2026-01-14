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
public class Preview {

    private Long id;
    private Project project;
    private String namespace; // Kubernetes namespace for the preview
    private String podName; // Kubernetes pod name for the preview
    private String previewUrl; // URL to access the preview
    private PreviewStatus status;
    private Instant startedAt;
    private Instant terminatedAt;
    private Instant createdAt;
}
