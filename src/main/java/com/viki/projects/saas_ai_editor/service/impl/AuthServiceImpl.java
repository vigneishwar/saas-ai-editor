package com.viki.projects.saas_ai_editor.service.impl;

import com.viki.projects.saas_ai_editor.dto.auth.AuthResponse;
import com.viki.projects.saas_ai_editor.dto.auth.LoginRequest;
import com.viki.projects.saas_ai_editor.dto.auth.SignupRequest;
import com.viki.projects.saas_ai_editor.entity.User;
import com.viki.projects.saas_ai_editor.error.BadRequestException;
import com.viki.projects.saas_ai_editor.mapper.UserMapper;
import com.viki.projects.saas_ai_editor.repository.UserRepository;
import com.viki.projects.saas_ai_editor.security.AuthUtil;
import com.viki.projects.saas_ai_editor.security.CustomUserDetails;
import com.viki.projects.saas_ai_editor.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthUtil authUtil;
    private final AuthenticationManager authenticationManager;


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

        String token = authUtil.generateToken(savedUser);

        return new AuthResponse(token, userMapper.userToUserProfileResponse(savedUser));
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
            if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
                throw new BadRequestException("Authentication principal is not of type CustomUserDetails");
            }
            User user = userDetails.getUser();
            String token = authUtil.generateToken(user);
            log.info("User {} logged in successfully", user.getUsername());
            return new AuthResponse(token, userMapper.userToUserProfileResponse(user));

        } catch (AuthenticationException e) {
            throw new BadRequestException("Invalid username or password");
        }
    }
}
