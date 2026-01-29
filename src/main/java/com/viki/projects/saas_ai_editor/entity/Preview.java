package com.viki.projects.saas_ai_editor.entity;

import com.viki.projects.saas_ai_editor.enums.PreviewStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Preview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    private String namespace; // Kubernetes namespace for the preview
    private String podName; // Kubernetes pod name for the preview
    private String previewUrl; // URL to access the preview
    private PreviewStatus status;
    private Instant startedAt;
    private Instant terminatedAt;
    private Instant createdAt;
}
