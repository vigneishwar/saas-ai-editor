package com.viki.projects.saas_ai_editor.controller;

import com.viki.projects.saas_ai_editor.dto.member.InviteMemberRequest;
import com.viki.projects.saas_ai_editor.dto.member.MemberResponse;
import com.viki.projects.saas_ai_editor.dto.member.UpdateRoleRequest;
import com.viki.projects.saas_ai_editor.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/members")
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @GetMapping
    public ResponseEntity<List<MemberResponse>>getProjectMembers(@PathVariable Long projectId){
        Long userId=1L; // TODO: get user id from auth context
        return ResponseEntity.ok(projectMemberService.getProjectMembers(projectId, userId));
    }
    @PostMapping
    public ResponseEntity<MemberResponse>inviteMember(@PathVariable Long projectId, @RequestBody InviteMemberRequest request){
        Long userId = 1L;
        return ResponseEntity.status(HttpStatus.CREATED).body((projectMemberService.inviteMember(projectId, request, userId)));
    }

    @PatchMapping("/{memberId}")
    public ResponseEntity<MemberResponse> updateMemberRole(@PathVariable Long projectId,
                                                           @PathVariable Long memberId,
                                                          @RequestBody UpdateRoleRequest request){
        Long userId = 1L;
        return ResponseEntity.ok(projectMemberService.updateMemberRole(projectId, memberId, request, userId));
    }

    @DeleteMapping("/{memberId}")
    public ResponseEntity<Void> removeMember(@PathVariable Long projectId,
                                                     @PathVariable Long memberId){
        Long userId = 1L;
        return ResponseEntity.noContent().build();
    }
}
