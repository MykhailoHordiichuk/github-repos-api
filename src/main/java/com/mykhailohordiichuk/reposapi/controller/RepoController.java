package com.mykhailohordiichuk.reposapi.controller;

import com.mykhailohordiichuk.reposapi.dto.RepositoryResponse;
import com.mykhailohordiichuk.reposapi.service.RepoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repos")
public class RepoController {
    private static final Logger log = LoggerFactory.getLogger(RepoController.class);

    private final RepoService repoService;

    public RepoController(RepoService repoService) {
        this.repoService = repoService;
    }

    @GetMapping("/{username}")
    public List<RepositoryResponse> getReposByUser(@PathVariable String username) {
        log.info("Controller: GET /api/repos/{}", username);
        return repoService.getRepositoriesByUsername(username);
    }
}