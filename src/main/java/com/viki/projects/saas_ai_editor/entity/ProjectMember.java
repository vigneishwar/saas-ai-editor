package com.viki.projects.saas_ai_editor.entity;

import java.time.Instant;

public class ProjectMember {
    private ProjectMemberId id;
    private Project project;
    private User user;
    private ProjectRole role;
    private Instant invitedAt;
    private Instant acceptedAt;
}
