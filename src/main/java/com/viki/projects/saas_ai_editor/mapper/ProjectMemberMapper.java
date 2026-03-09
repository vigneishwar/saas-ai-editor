package com.viki.projects.saas_ai_editor.mapper;


import com.viki.projects.saas_ai_editor.dto.member.MemberResponse;
import com.viki.projects.saas_ai_editor.entity.ProjectMember;
import com.viki.projects.saas_ai_editor.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // This tells MapStruct to generate a Spring bean for this mapper
public interface ProjectMemberMapper {

    @Mapping(target = "role", constant = "OWNER") // Set the 'role' field in MemberResponse to 'OWNER' for the project owner
    @Mapping(target = "userId", source = "id") // Map the 'id' field in User to 'userId' in MemberResponse
    MemberResponse toMemberResponseFromOwner(User owner);


    @Mapping(target = "userId", source = "user.id" )
    @Mapping(target = "email", source = "user.email" )
    @Mapping(target = "name", source = "user.name" )
    MemberResponse toMemberResponseFromMember(ProjectMember projectMember);
}
