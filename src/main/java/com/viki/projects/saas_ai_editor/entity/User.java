package com.viki.projects.saas_ai_editor.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Column(nullable = false)
    private String name;

    @CreationTimestamp
    private Instant created_at;

    @UpdateTimestamp
    private Instant updated_at;

    private Instant deleted_at;

}
