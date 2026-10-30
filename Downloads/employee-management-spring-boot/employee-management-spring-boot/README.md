# PeopleDesk — Spring Boot Employee Management

This project converts the original Node.js/Express backend to Java 21 + Spring Boot. The existing HTML, CSS and JavaScript frontend is retained and served by Spring Boot from `src/main/resources/static`.

## Requirements
- Java 21
- Maven 3.9+
- IntelliJ IDEA (or another Java IDE)

## Run locally
```bash
mvn clean test
mvn spring-boot:run
```
Open http://localhost:8080.

## Build executable JAR
```bash
mvn clean package
java -jar target/employee-management.jar
```

## API
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/health` | Health status |
| GET | `/api/stats` | Dashboard totals |
| GET | `/api/employees` | List employees |
| GET | `/api/employees?search=aarav` | Search employees |
| GET | `/api/employees?department=Engineering&status=Active` | Filter employees |
| GET | `/api/employees/{id}` | Get one employee |
| POST | `/api/employees` | Create employee |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |

Example JSON:
```json
{
  "name": "Samira Ali",
  "email": "samira.ali@example.com",
  "department": "Engineering",
  "role": "Software Engineer",
  "status": "Active",
  "joinedDate": "2025-01-15"
}
```

## Persistence
H2 file database is stored under `./data` by default. On first startup, five sample employees are inserted. Data is retained across restarts as long as the database directory or Docker volume is retained.

## Docker
```bash
mvn clean package
docker compose up -d --build
```
Open http://localhost:8080.

## GitHub Actions
The workflow at `.github/workflows/deploy.yml` builds and tests the Spring Boot application on pushes and pull requests to `main`, then uploads the JAR as an artifact. It does not deploy to EC2 yet. Add EC2 deployment after creating the instance and configuring secure GitHub repository secrets.

## Important
This is a demo application. It does not include authentication or authorization. Do not store real employee data until security, access control, backups and production database operations are added.
