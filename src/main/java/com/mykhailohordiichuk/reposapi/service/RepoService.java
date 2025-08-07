package com.mykhailohordiichuk.reposapi.service;

import com.mykhailohordiichuk.reposapi.client.GitHubClient;
import com.mykhailohordiichuk.reposapi.dto.RepositoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RepoService {
    private final GitHubClient gitHubClient;

    public List<RepositoryResponse> getRepositoriesByUsername(String username) {
        return gitHubClient.getUserRepos(username);
    }
}
