package com.viki.projects.saas_ai_editor.service.impl;

import com.viki.projects.saas_ai_editor.dto.auth.AuthResponse;
import com.viki.projects.saas_ai_editor.dto.auth.LoginRequest;
import com.viki.projects.saas_ai_editor.dto.auth.SignupRequest;
import com.viki.projects.saas_ai_editor.entity.User;
import com.viki.projects.saas_ai_editor.error.BadRequestException;
import com.viki.projects.saas_ai_editor.mapper.UserMapper;
import com.viki.projects.saas_ai_editor.repository.UserRepository;
import com.viki.projects.saas_ai_editor.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public AuthResponse signup(SignupRequest request) {

        // Check if user already exists
        userRepository.findByUsername(request.username()).ifPresent(user -> {
            throw new BadRequestException("Username already exists: " + request.username());
        });

        User user = userMapper.signupRequestToUser(request);
        // Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return new AuthResponse("token", userMapper.userToUserProfileResponse(savedUser));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
