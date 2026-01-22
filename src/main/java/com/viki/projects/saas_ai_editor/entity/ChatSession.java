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
public class ChatSession {
    private Project project;
    private User user;
    private String title;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;
}
