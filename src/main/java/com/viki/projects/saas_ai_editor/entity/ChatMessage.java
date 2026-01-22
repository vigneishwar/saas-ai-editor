package com.viki.projects.saas_ai_editor.entity;


import com.viki.projects.saas_ai_editor.enums.MessageRole;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {
    private Long id;
    private ChatSession chatSession;
    private User user;
    private String toolCalls; // JSON array representing tool calls
    private MessageRole role;
    private String content;
    private Integer tokensUsed; // number of tokens used in this message
    private Instant createdAt;
}
