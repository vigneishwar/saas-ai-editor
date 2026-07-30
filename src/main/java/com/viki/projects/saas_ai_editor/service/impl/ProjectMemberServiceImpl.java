package com.viki.projects.saas_ai_editor.service.impl;

import com.viki.projects.saas_ai_editor.dto.member.InviteMemberRequest;
import com.viki.projects.saas_ai_editor.dto.member.MemberResponse;
import com.viki.projects.saas_ai_editor.dto.member.UpdateRoleRequest;
import com.viki.projects.saas_ai_editor.entity.Project;
import com.viki.projects.saas_ai_editor.entity.ProjectMember;
import com.viki.projects.saas_ai_editor.entity.ProjectMemberId;
import com.viki.projects.saas_ai_editor.entity.User;
import com.viki.projects.saas_ai_editor.error.ResourceNotFoundException;
import com.viki.projects.saas_ai_editor.mapper.ProjectMemberMapper;
import com.viki.projects.saas_ai_editor.repository.ProjectMemberRepository;
import com.viki.projects.saas_ai_editor.repository.ProjectRepository;
import com.viki.projects.saas_ai_editor.repository.UserRepository;
import com.viki.projects.saas_ai_editor.security.AuthUtil;
import com.viki.projects.saas_ai_editor.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMemberMapper projectMemberMapper;
    private final UserRepository userRepository;
    private final AuthUtil authUtil;

    @Override
    public List<MemberResponse> getProjectMembers(Long projectId) {
        Long userId = authUtil.getUserId();
        Project project = getAccessibleProjectById(projectId, userId); // check if the user has access to the project

        return (
                projectMemberRepository.findByIdProjectId(projectId)
                        .stream()
                        .map(projectMemberMapper::toMemberResponseFromMember)
                        .toList());
    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request) {
        Long userId = authUtil.getUserId();

        Project project = getAccessibleProjectById(projectId, userId); // check if the user has access to the project

        User invitee = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("User with email " + request.username() + " not found"));

        if(invitee.getId().equals(userId)){
            throw new RuntimeException("User cannot invite yourself");
        }

        // check if the user is already a member of the project
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, invitee.getId());
        if(projectMemberRepository.existsById(projectMemberId)){
            throw new RuntimeException("User is already a member of the project");
        }

        ProjectMember member = ProjectMember.builder()
                .id(projectMemberId)
                .project(project)
                .user(invitee)
                .role(request.role())
                .invitedAt(Instant.now())
                .build();
        member = projectMemberRepository.save(member);
        return projectMemberMapper.toMemberResponseFromMember(member);
    }

    @Override
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateRoleRequest request) {
        Long userId = authUtil.getUserId();

        Project project = getAccessibleProjectById(projectId, userId); // check if the user has access to the project

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        ProjectMember member = projectMemberRepository.findById(projectMemberId)
                .orElseThrow(() -> new RuntimeException("Project member not found"));

        member.setRole(request.role());
        member = projectMemberRepository.save(member);
        return projectMemberMapper.toMemberResponseFromMember(member);
    }

    @Override
    public void removeProjectMember(Long projectId, Long memberId) {
        Long userId = authUtil.getUserId();

        Project project = getAccessibleProjectById(projectId, userId); // check if the user has access to the project

        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        if(!projectMemberRepository.existsById(projectMemberId)){
            throw new RuntimeException("Project member not found");
        }

        projectMemberRepository.deleteById(projectMemberId);
    }

    private Project getAccessibleProjectById(Long id, Long userId) {
        return projectRepository.findAccessibleProjectById(id, userId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }
}
