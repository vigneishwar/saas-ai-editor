package com.viki.projects.saas_ai_editor.service;

import com.viki.projects.saas_ai_editor.dto.project.FileContentResponse;
import com.viki.projects.saas_ai_editor.dto.project.FileNode;

import java.util.List;

public interface FileService {
    List<FileNode> getFileTree(Long projectID, Long userId);

    FileContentResponse getFileContent(Long projectId, String path, Long userId);
}
