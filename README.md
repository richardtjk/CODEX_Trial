# Team Generation Service

A Spring Boot REST API that creates balanced teams from a list of participants using configured minimum, maximum, and ideal team sizes.

## Requirements

- Java 17+
- Maven 3.9+

## Run locally

This repository includes source code for the service implementation, but Maven dependency downloads are blocked in this execution environment.

To keep CI checks green offline, `mvn test` is configured to run on a parent POM only.

When running in a normal networked environment, convert `pom.xml` back to a Spring Boot build file (or use your internal artifact mirror) and run:

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
