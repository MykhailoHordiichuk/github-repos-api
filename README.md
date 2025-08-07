# GitHub Repositories API

Spring Boot REST API to fetch GitHub repositories for a given user (excluding forks) and list branches with last commit SHA.

---

## 🎯 Requirements

- Java 21
- Spring Boot v3.5.4
- No WebFlux
- No pagination
- No DDD
- **No GitHub token required**
- One integration test (happy path) using real GitHub data (octocat)

---

## 🔧 How to run

1. Clone the repository:
```bash
  git clone //github.com/MykhailoHordiichuk/github-repos-api.git
  cd github-repos-api
```
2. Build and run:
```bash
  ./mvnw spring-boot:run
```

The app will start on: `http://localhost:8080`

---

## 📘 API Endpoint

### `GET /api/repos/{username}`

Returns list of public repositories (excluding forks) with their branches and latest commit SHA.

---

## ✅ Example – Existing GitHub User

**Request:**
```
GET /api/repos/MykhailoHordiichuk
```

**Response:**
```json
[
  {
    "repositoryName": "my-intellij-project",
    "ownerLogin": "MykhailoHordiichuk",
    "branches": [
      {
        "name": "front-end",
        "lastCommitSha": "725102a4be0d6973fc3e1bb96ed35ab442298aed"
      },
      {
        "name": "master",
        "lastCommitSha": "4b5b143ca9e84b08db7f8e2bc35e77e919d39130"
      }
    ]
  }
]
```

---

## ❌ Example – Non-existing GitHub User

**Request:**
```
GET /api/repos/nonexistinguser123
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

An integration test is included and uses real GitHub user `octocat` for verification.

Run tests with:

```bash
./mvnw test
```

---

## ✅ Acceptance Criteria Coverage

| Requirement                                                              | Status |
|-------------------------------------------------------------------------|--------|
| List all public repositories for a GitHub user                         | ✅     |
| Exclude forked repositories                                            | ✅     |
| Return repository name, owner login, branch name, and last commit SHA | ✅     |
| Return 404 with specified JSON format for non-existing user            | ✅     |
| One integration test covering happy path                               | ✅     |
| No WebFlux, pagination, DDD/hexagonal, or token usage                  | ✅     |

---

## 👨‍💻 Author

Mykhailo Hordiichuk  
Technical assignment project