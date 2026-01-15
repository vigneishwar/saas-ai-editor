package com.viki.projects.saas_ai_editor.entity;

import java.time.Instant;

public class UsageLog {

    private Long id;
    private User user;
    private Project project;
    private String action;
    private Integer tokensUsed;
    private Integer durationMs;
    private String metadata; // JSON string for additional metadata
    private Instant createdAt;
}
