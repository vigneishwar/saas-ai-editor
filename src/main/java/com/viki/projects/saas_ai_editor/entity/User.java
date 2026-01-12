package com.viki.projects.saas_ai_editor.entity;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    private Long id;

    private String email;

    private String password_hash;

    private String name;

    private String avatar_url;

    private Instant created_at;

    private Instant updated_at;

    private Instant deleted_at;

}
