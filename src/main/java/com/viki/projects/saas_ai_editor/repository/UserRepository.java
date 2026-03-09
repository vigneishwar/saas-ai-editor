package com.viki.projects.saas_ai_editor.repository;

import com.viki.projects.saas_ai_editor.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
