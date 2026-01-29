package com.viki.projects.saas_ai_editor.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;



@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsageLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    private String action;
    private Integer tokensUsed;
    private Integer durationMs;
    private String metadata; // JSON string for additional metadata

    @CreationTimestamp
    private Instant createdAt;
}
