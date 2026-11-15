# Art Catalogue

React + Vite frontend, Java 21 / Spring Boot REST API, and PostgreSQL.

## Run
Requirements: Docker Engine and Docker Compose.

```bash
docker compose up --build
```

Open http://localhost:5173. API: http://localhost:8080/api/artworks.
Health: http://localhost:8080/actuator/health.

Stop with `docker compose down`. Remove database data with `docker compose down -v`.

## API
- `GET /api/artworks?search=&category=`
- `GET /api/artworks/{id}`
- `POST /api/artworks`
- `PUT /api/artworks/{id}`
- `DELETE /api/artworks/{id}`

POST/PUT JSON fields: `title`, `artist`, `category`, `yearCreated`, `medium`, `price`, `imageUrl`, `description`.

## Important
This is the first application milestone, not yet safe for public production deployment. Write APIs currently have no authentication, Hibernate schema auto-update is enabled for local development, and the sample database credentials are for local use only. We will harden these before deployment.

## Structure
- `backend/` Spring Boot API
- `frontend/` React application
- `docker-compose.yml` local development environment
