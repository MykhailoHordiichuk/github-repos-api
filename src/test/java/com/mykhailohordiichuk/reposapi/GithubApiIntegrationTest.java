package com.mykhailohordiichuk.reposapi;

import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.RestClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GithubApiIntegrationTest {

    private static WireMockServer wireMockServer;

    @LocalServerPort
    private int port;

    @Autowired
    private RestClient.Builder restClientBuilder;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(0);
        wireMockServer.start();

        configureFor("localhost", wireMockServer.port());

        stubFor(get(urlEqualTo("/users/octocat/repos"))
                .willReturn(okJson("""
                        [
                          {"name": "Repo1", "fork": false, "owner": {"login": "octocat"}},
                          {"name": "ForkedRepo", "fork": true, "owner": {"login": "octocat"}}
                        ]
                        """)));

        stubFor(get(urlEqualTo("/repos/octocat/Repo1/branches"))
                .willReturn(okJson("""
                        [
                          {"name": "main", "commit": {"sha": "abc123"}},
                          {"name": "dev", "commit": {"sha": "def456"}}
                        ]
                        """)));

        stubFor(get(urlEqualTo("/users/unknownuser/repos"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                    "status":404,
                                    "message":"GitHub user not found: unknownuser"
                                }
                                """)));
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("github.api.base-url", () -> "http://localhost:" + wireMockServer.port());
    }

    @Test
    void shouldHandleHappyPathAndNotFound() {
        RestClient client = restClientBuilder.baseUrl("http://localhost:" + port).build();

        var successResponse = client.get()
                .uri("/api/repos/octocat")
                .exchange((req, res) -> ResponseEntity
                        .status(res.getStatusCode())
                        .headers(res.getHeaders())
                        .body(res.bodyTo(String.class)));

        assertThat(successResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(successResponse.getBody()).isEqualToIgnoringWhitespace("""
                [
                  {
                    "repositoryName": "Repo1",
                    "ownerLogin": "octocat",
                    "branches": [
                      {"name": "main", "lastCommitSha": "abc123"},
                      {"name": "dev", "lastCommitSha": "def456"}
                    ]
                  }
                ]
                """);

        var notFoundResponse = client.get()
                .uri("/api/repos/unknownuser")
                .exchange((req, res) -> ResponseEntity
                        .status(res.getStatusCode())
                        .headers(res.getHeaders())
                        .body(res.bodyTo(String.class)));

        assertThat(notFoundResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(notFoundResponse.getBody()).isEqualToIgnoringWhitespace("""
                {
                    "status":404,
                    "message":"GitHub user not found: unknownuser"
                }
                """);
    }
}