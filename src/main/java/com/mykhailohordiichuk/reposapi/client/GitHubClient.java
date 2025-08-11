package com.mykhailohordiichuk.reposapi.client;

import com.mykhailohordiichuk.reposapi.dto.BranchResponse;
import com.mykhailohordiichuk.reposapi.dto.RepositoryResponse;
import com.mykhailohordiichuk.reposapi.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Arrays;
import java.util.List;

@Component
public class GitHubClient {

    private static final Logger log = LoggerFactory.getLogger(GitHubClient.class);

    private final RestClient restClient;

    public GitHubClient(RestClient.Builder builder,
                        @Value("${github.api.base-url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public List<RepositoryResponse> getUserRepos(String username) {
        log.info("Fetching repositories for GitHub user '{}'", username);
        try {
            GitHubRepo[] repos = restClient.get()
                    .uri("/users/{username}/repos", username)
                    .retrieve()
                    .onStatus(status -> status.value() == 404, (req, res) -> {
                        log.warn("GitHub returned 404 for user {}", username);
                        throw new UserNotFoundException(username);
                    })
                    .body(GitHubRepo[].class);

            if (repos == null || repos.length == 0) {
                log.info("No repositories found for user {}", username);
                return List.of();
            }

            var result = Arrays.stream(repos)
                    .filter(repo -> !repo.fork())
                    .map(repo -> new RepositoryResponse(
                            repo.name(),
                            repo.owner().login(),
                            getBranches(username, repo.name())
                    ))
                    .toList();

            log.info("Returning {} non-fork repositories for user {}", result.size(), username);
            return result;
        } catch (UserNotFoundException ex) {
            throw ex; // handled by controller advice
        } catch (RestClientException ex) {
            log.error("Error while calling GitHub API for user {}: {}", username, ex.getMessage(), ex);
            throw new RuntimeException("Failed to fetch repositories from GitHub", ex);
        }
    }

    private List<BranchResponse> getBranches(String username, String repoName) {
        log.debug("Fetching branches for {}/{}", username, repoName);
        try {
            GitHubBranch[] branches = restClient.get()
                    .uri("/repos/{username}/{repo}/branches", username, repoName)
                    .retrieve()
                    .body(GitHubBranch[].class);

            if (branches == null || branches.length == 0) {
                log.debug("No branches found for {}/{}", username, repoName);
                return List.of();
            }

            return Arrays.stream(branches)
                    .map(branch -> new BranchResponse(branch.name(), branch.commit().sha()))
                    .toList();
        } catch (RestClientException ex) {
            log.error("Error while fetching branches for {}/{}: {}", username, repoName, ex.getMessage(), ex);
            throw new RuntimeException("Failed to fetch branches from GitHub", ex);
        }
    }

    private record GitHubRepo(String name, boolean fork, Owner owner) {}
    private record Owner(String login) {}
    private record GitHubBranch(String name, Commit commit) {}
    private record Commit(String sha) {}
}