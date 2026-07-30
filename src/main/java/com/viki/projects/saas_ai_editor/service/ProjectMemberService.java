package com.viki.projects.saas_ai_editor.service;

import com.viki.projects.saas_ai_editor.dto.member.InviteMemberRequest;
import com.viki.projects.saas_ai_editor.dto.member.MemberResponse;
import com.viki.projects.saas_ai_editor.dto.member.UpdateRoleRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProjectMemberService {
    List<MemberResponse> getProjectMembers(Long projectId);

    MemberResponse inviteMember(Long projectId, InviteMemberRequest request);

    MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateRoleRequest request);

    void removeProjectMember(Long projectId, Long memberId);

}
