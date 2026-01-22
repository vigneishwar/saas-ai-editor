package com.viki.projects.saas_ai_editor.service;

import com.viki.projects.saas_ai_editor.dto.auth.UserProfileResponse;

public interface UserService {
    UserProfileResponse getUserProfile(Long userId);
}
