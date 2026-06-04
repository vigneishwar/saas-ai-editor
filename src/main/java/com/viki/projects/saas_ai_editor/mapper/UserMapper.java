package com.viki.projects.saas_ai_editor.mapper;


import com.viki.projects.saas_ai_editor.dto.auth.AuthResponse;
import com.viki.projects.saas_ai_editor.dto.auth.SignupRequest;
import com.viki.projects.saas_ai_editor.dto.auth.UserProfileResponse;
import com.viki.projects.saas_ai_editor.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // This tells MapStruct to generate a Spring bean for this mapper
public interface UserMapper {

    User signupRequestToUser(SignupRequest signupRequest);

    UserProfileResponse userToUserProfileResponse(User user);
}
