package com.mykhailohordiichuk.reposapi;

import com.mykhailohordiichuk.reposapi.dto.RepositoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GithubApiIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnRepositoriesForValidUser() {
        // given
        String username = "octocat";

        // when
        ResponseEntity<RepositoryResponse[]> response = restTemplate.getForEntity(
                "/api/repos/" + username,
                RepositoryResponse[].class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody()[0].getRepositoryName()).isNotNull();
        assertThat(response.getBody()[0].getOwnerLogin()).isEqualTo("octocat");
    }
}