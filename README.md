# Team Generation Service

A Spring Boot REST API that creates balanced teams from a list of participants using configured minimum, maximum, and ideal team sizes.

## Requirements

- Java 17+
- Maven 3.9+

## Run locally

```bash
mvn spring-boot:run
```

Service starts on `http://localhost:8080`.

## API

### `POST /api/teams/generate`

#### Request body

```json
{
  "participants": ["Alice", "Bob", "Charlie", "Dana", "Eli", "Fran", "Gabe"],
  "minTeamSize": 2,
  "maxTeamSize": 4,
  "idealTeamSize": 3
}
```

#### Response body

```json
{
  "teams": [
    {
      "name": "Team 1",
      "members": ["Alice", "Bob", "Charlie", "Dana"]
    },
    {
      "name": "Team 2",
      "members": ["Eli", "Fran", "Gabe"]
    }
  ]
}
```

## Validation rules

- `participants` must not be empty.
- `minTeamSize <= maxTeamSize`
- `idealTeamSize` must be between `minTeamSize` and `maxTeamSize`.
- If no valid team count exists for the given participant count and limits, the API returns `400`.

## GitHub deployment to an IP address

This repository includes a GitHub Actions workflow at `.github/workflows/deploy.yml` that deploys the app to a Linux server using SSH and Docker Compose.

### 1) Prepare your server (the target IP)

- Install Docker Engine and Docker Compose plugin.
- Ensure SSH access is enabled.
- Open the app port (default `8080`) in the firewall/security group.

### 2) Add GitHub repository secrets

In your GitHub repo: **Settings → Secrets and variables → Actions → New repository secret**.

Required:
- `DEPLOY_HOST`: your server IP address (example: `203.0.113.10`)
- `DEPLOY_USER`: SSH user on the server (example: `ubuntu`)
- `DEPLOY_SSH_KEY`: private key content for SSH auth

Optional:
- `DEPLOY_PORT`: SSH port (default `22`)
- `DEPLOY_PATH`: deployment folder on server (default `/opt/team-service`)
- `APP_PORT`: public host port for the service (default `8080`)

### 3) Deploy

- Push to `main`, or
- Manually run **Actions → Deploy to VM → Run workflow**.

The workflow will:
1. Copy repository files to your server.
2. Run `docker compose up -d --build` on the server.

After deployment, access:

- `http://<DEPLOY_HOST>:<APP_PORT>/api/teams/generate`
