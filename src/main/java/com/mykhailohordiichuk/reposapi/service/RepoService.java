package com.mykhailohordiichuk.reposapi.service;

import com.mykhailohordiichuk.reposapi.client.GitHubClient;
import com.mykhailohordiichuk.reposapi.dto.RepositoryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepoService {
    private static final Logger log = LoggerFactory.getLogger(RepoService.class);

    private final GitHubClient gitHubClient;

    public RepoService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public List<RepositoryResponse> getRepositoriesByUsername(String username) {
        log.info("Service: getRepositoriesByUsername for {}", username);
        return gitHubClient.getUserRepos(username);
    }
}