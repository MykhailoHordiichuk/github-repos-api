package com.mykhailohordiichuk.reposapi.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mykhailohordiichuk.reposapi.dto.BranchResponse;
import com.mykhailohordiichuk.reposapi.dto.RepositoryResponse;
import com.mykhailohordiichuk.reposapi.exception.UserNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GitHubClient {
    private final RestClient restClient;

    public GitHubClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.github.com")
                .build();
    }

    public List<RepositoryResponse> getUserRepos(String username) {
        try {
            GitHubRepo[] repos = restClient.get()
                    .uri("/users/{username}/repos", username)
                    .retrieve()
                    .onStatus(status -> status.value() == 404, (req, res) -> {
                        throw new UserNotFoundException(username);
                    })
                    .body(GitHubRepo[].class);

            if (repos == null) {
                throw new RuntimeException("GitHub API returned null");
            }

            return Arrays.stream(repos)
                    .filter(repo -> !repo.fork)
                    .map(repo -> new RepositoryResponse(
                            repo.name,
                            repo.owner.login,
                            getBranches(username, repo.name)
                    ))
                    .collect(Collectors.toList());

        } catch (RestClientException e) {
            throw new RuntimeException("Failed to fetch repositories from GitHub", e);
        }
    }

    private List<BranchResponse> getBranches(String username, String repoName) {
        try {
            GitHubBranch[] branches = restClient.get()
                    .uri("/repos/{username}/{repo}/branches", username, repoName)
                    .retrieve()
                    .body(GitHubBranch[].class);

            if (branches == null){
                throw new RuntimeException("GitHub API returned null branches");
            }

            return Arrays.stream(branches)
                    .map(branch -> new BranchResponse(
                            branch.name,
                            branch.commit.sha
                    ))
                    .collect(Collectors.toList());
        }catch (RestClientException e){
            throw new RuntimeException("Failed to fetch branches from GitHub", e);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GitHubRepo {
        public String name;
        public boolean fork;
        public Owner owner;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Owner {
        public String login;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class GitHubBranch {
        public String name;
        public Commit commit;
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Commit {
        public String sha;
    }
}
