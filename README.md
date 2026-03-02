# Team Generation Service

A Spring Boot REST API that creates balanced teams from a list of participants using configured minimum, maximum, and ideal team sizes.

## Requirements

- Java 21+
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

## API test page

A simple browser-based tester is available at `src/main/resources/static/index.html`.

When the service is running, open:

- `http://<DEPLOY_HOST>:<APP_PORT>/`

It will send requests to `POST /api/teams/generate` and display the JSON response.

## Validation rules

- `participants` must not be empty.
- `minTeamSize <= maxTeamSize`
- `idealTeamSize` must be between `minTeamSize` and `maxTeamSize`.
- If no valid team count exists for the given participant count and limits, the API returns `400`.

## GitHub deployment to an IP address

This repository includes a GitHub Actions workflow at `.github/workflows/deploy.yml` that builds the JAR in GitHub and deploys it directly to a Linux server over SSH (no Docker required).

### 1) Prepare your server (the target IP)

- Ensure SSH access is enabled.
- The deploy user must have `sudo` privileges to install Java and manage systemd.
- Open the app port (default `8080`) in firewall/security group.

### 2) Add GitHub repository secrets

In your GitHub repo: **Settings → Secrets and variables → Actions → New repository secret**.

Required:
- `DEPLOY_HOST`: your server IP address (example: `203.0.113.10`)
- `DEPLOY_USER`: SSH user on the server (example: `ubuntu`)
- `DEPLOY_SSH_KEY`: private key content for SSH auth

Optional:
- `DEPLOY_PORT`: SSH port (default `22`)
- `DEPLOY_PATH`: temp upload directory on server (default `/tmp/team-service-deploy`)
- `APP_PORT`: host port for the service (default `8080`)

### Connectivity test action

You can validate SSH connectivity before deployment by running:

- **Actions → Test server connectivity → Run workflow**

This workflow uses `DEPLOY_HOST`, `DEPLOY_USER`, and `DEPLOY_SSH_KEY` (plus optional `DEPLOY_PORT`) and only checks that SSH access works.

### 3) Deploy

- Push to `main`, or
- Manually run **Actions → Deploy to VM (direct Spring Boot) → Run workflow**.

The workflow will:
1. Build `target/team-service-0.0.1-SNAPSHOT.jar` in GitHub Actions.
2. Copy the JAR + deploy script to your server.
3. Install Java 21 if needed.
4. Install/update a systemd service named `team-service` and restart it.

The deploy script now waits for apt/dpkg locks to clear before installing Java on Debian/Ubuntu hosts, reducing failures from transient lock contention.

Useful server commands:

```bash
sudo systemctl status team-service
sudo journalctl -u team-service -f
```

After deployment, access:

- `http://<DEPLOY_HOST>:<APP_PORT>/api/teams/generate`
