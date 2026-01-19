# GitHub Repositories API

A Spring Boot REST API to fetch all non-fork GitHub repositories for a given user,
including each branch with its last commit SHA.

---

## 📋 Requirements

- **Java** 21 (LTS)
- **Spring Boot** 3.5.3
- Maven (or use included `mvnw` wrapper)
- Internet connection (for calling the GitHub API)

---

## 🛠 Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/MykhailoHordiichuk/github-repos-api.git
   cd github-repos-api
   ```

2. **Build and run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 🚀 Usage

### Endpoint

**GET** `/api/repos/{username}`

Retrieves all **non-fork** public repositories for the given GitHub username, including each branch and its latest commit SHA.

### Example — Existing GitHub User

**Request:**
```bash
 curl http://localhost:8080/api/repos/octocat
```

**Response:**
```json
[
  {
    "repositoryName": "Hello-World",
    "ownerLogin": "octocat",
    "branches": [
      { "name": "main", "lastCommitSha": "abc123" },
      { "name": "dev", "lastCommitSha": "def456" }
    ]
  }
]
```

### Example — Non-existing GitHub User

**Request:**
```bash
 curl http://localhost:8080/api/repos/nonexistinguser123
```

**Response:**
```json
{
  "status": 404,
  "message": "GitHub user not found: nonexistinguser123"
}
```

---

## 🧪 Running Tests

Integration tests are included and **use WireMock** to mock the GitHub API.

Run all tests:
```bash
 ./mvnw test
```

---

## 📦 Project Structure

- **`client/`** — HTTP client for GitHub API (uses Spring `RestClient`)
- **`config/`** — Configuration beans
- **`controller/`** — REST controller
- **`dto/`** — Data transfer objects (`record` classes)
- **`exception/`** — Exception classes and handlers
- **`service/`** — Business logic
- **`test/`** — Integration tests (with WireMock)

---

## ✅ Acceptance Criteria Coverage

| Requirement                                                              | Status |
|---------------------------------------------------------------------------|--------|
| List all public repositories for a GitHub user                           | ✅     |
| Exclude forked repositories                                              | ✅     |
| Return repository name, owner login, branch name, and last commit SHA    | ✅     |
| Return 404 with specified JSON format for non-existing user              | ✅     |
| One integration test covering happy path (WireMock)                      | ✅     |
| No WebFlux, pagination, DDD/hexagonal                                    | ✅     |

---

## 🤝 Contributing

This repository is for recruitment purposes only and does not accept external contributions.

---

## 📄 License

This project is licensed under the [MIT License](LICENSE).

---

## 📌 Project Status

Completed — no further development planned.

---

**Author:** Mykhailo Hordiichuk  
Technical assignment project
