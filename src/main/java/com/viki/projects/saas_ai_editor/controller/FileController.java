package com.viki.projects.saas_ai_editor.controller;

import com.viki.projects.saas_ai_editor.dto.project.FileContentResponse;
import com.viki.projects.saas_ai_editor.dto.project.FileNode;
import com.viki.projects.saas_ai_editor.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/files")
@RequiredArgsConstructor
public class FileController {

    private FileService fileService;

    @GetMapping()
    public ResponseEntity<List<FileNode>>getFileTree(@PathVariable Long projectID){
        Long userId=1L; // TODO: get user id from auth context
        return ResponseEntity.ok(fileService.getFileTree(projectID, userId));
    }

    @GetMapping("/{*path}")
    public ResponseEntity<FileContentResponse> getFile(@PathVariable Long projectId, @PathVariable String path){
        Long userId=1L; // TODO: get user id from auth context
        return ResponseEntity.ok(fileService.getFileContent(projectId, path, userId));
    }
}
