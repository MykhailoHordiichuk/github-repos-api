package com.mykhailohordiichuk.reposapi.controller;

import com.mykhailohordiichuk.reposapi.dto.RepositoryResponse;
import com.mykhailohordiichuk.reposapi.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/repos")
@RequiredArgsConstructor
public class RepoController {
    private final RepoService repoService;

    @GetMapping("/{username}")
    public ResponseEntity<List<RepositoryResponse>> getReposByUser (@PathVariable String username) {
        List<RepositoryResponse> repositories = repoService.getRepositoriesByUsername(username);
        return ResponseEntity.ok(repositories);
    }
}
