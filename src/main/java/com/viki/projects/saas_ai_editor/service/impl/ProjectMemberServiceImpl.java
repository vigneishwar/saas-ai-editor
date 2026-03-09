package com.viki.projects.saas_ai_editor.service.impl;

import com.viki.projects.saas_ai_editor.dto.member.InviteMemberRequest;
import com.viki.projects.saas_ai_editor.dto.member.MemberResponse;
import com.viki.projects.saas_ai_editor.dto.member.UpdateRoleRequest;
import com.viki.projects.saas_ai_editor.entity.Project;
import com.viki.projects.saas_ai_editor.entity.ProjectMember;
import com.viki.projects.saas_ai_editor.entity.ProjectMemberId;
import com.viki.projects.saas_ai_editor.entity.User;
import com.viki.projects.saas_ai_editor.mapper.ProjectMemberMapper;
import com.viki.projects.saas_ai_editor.repository.ProjectMemberRepository;
import com.viki.projects.saas_ai_editor.repository.ProjectRepository;
import com.viki.projects.saas_ai_editor.repository.UserRepository;
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

    @Override
    public List<MemberResponse> getProjectMembers(Long projectId, Long userId) {
        Project project = getAccessibleProjectById(projectId, userId); // check if the user has access to the project

        List<MemberResponse> memberResponseList = new ArrayList<>();
        memberResponseList.add(projectMemberMapper.toMemberResponseFromOwner(project.getOwner())); // user can also be the owner of the project, so we add the owner to the member list along with the members

        memberResponseList.addAll(
                projectMemberRepository.findByIdProjectId(projectId)
                        .stream()
                        .map(projectMemberMapper::toMemberResponseFromMember)
                        .toList());
        return memberResponseList;
    }

    @Override
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request, Long userId) {

        Project project = getAccessibleProjectById(projectId, userId); // check if the user has access to the project

        if(!project.getOwner().getId().equals(userId)){
            throw new RuntimeException("Only project owner can invite members");
        }

        User invitee = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User with email " + request.email() + " not found"));

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
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateRoleRequest request, Long userId) {
        return null;
    }

    @Override
    public MemberResponse deleteProjectMember(Long projectId, Long memberId, Long userId) {
        return null;
    }

    private Project getAccessibleProjectById(Long id, Long userId) {
        return projectRepository.findAccessibleProjectById(id, userId)
                .orElseThrow(() -> new RuntimeException("Project not found or access denied"));
    }
}
