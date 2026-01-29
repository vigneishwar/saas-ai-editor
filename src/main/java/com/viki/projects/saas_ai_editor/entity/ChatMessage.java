package com.viki.projects.saas_ai_editor.entity;


import com.viki.projects.saas_ai_editor.enums.MessageRole;
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
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_session_id", nullable = false)
    private ChatSession chatSession;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private String toolCalls; // JSON array representing tool calls
    private MessageRole role;
    private String content;
    private Integer tokensUsed; // number of tokens used in this message

    @CreationTimestamp
    private Instant createdAt;
}
