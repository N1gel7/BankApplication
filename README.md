# NeoBank - Modern Banking Application

A containerized backend banking application built with **Spring Boot** and **Java 17**.

## 🚀 Key Capabilities & Architecture

- **Containerization & Orchestration:** Fully dockerized environment using `Docker` and `Docker Compose` for seamless deployment of the API, PostgreSQL, and Redis instances.
- **Database Migrations:** Automated, version-controlled schema migrations using **Flyway**, ensuring consistent database structures across all environments.
- **Advanced Security:** Stateless authentication via **JSON Web Tokens (JWT)**, HTTP-only secure cookies, and robust **Spring Security** integration.
- **Distributed Caching:** Leverages **Redis** to cache heavy database queries (e.g., pending KYC documents), significantly improving API response times and reducing database load.
- **API Rate Limiting:** Implemented **Bucket4j** to defend against brute-force attacks and API abuse (e.g., limiting login attempts and enforcing transfer delays).
- **Data Persistence:** **PostgreSQL** coupled with **Spring Data JPA/Hibernate** for reliable, transactional data persistence. 
- **Administrative Portal:** Complete frontend admin dashboard to evaluate KYC requests and monitor global transactions, integrating asynchronously with the backend API via Fetch.

## 🛠 Tech Stack

- **Backend:** Java 17, Spring Boot, Spring Security, Spring Data JPA
- **Databases:** PostgreSQL (Relational Data), Redis (Caching)
- **Tooling:** Maven, Flyway, Docker, Docker Compose
- **Frontend:** Vanilla JavaScript, HTML5, CSS3

## 📦 Getting Started

### Prerequisites
- [Docker](https://www.docker.com/products/docker-desktop) and Docker Compose installed.

### Run the Application
1. Clone the repository and navigate into the project root.
2. Build and start the services using Docker Compose:
   ```bash
   docker compose up --build -d
   ```
3. The API will be available at `http://localhost:8080`.
4. Flyway will automatically run the latest SQL migrations (located in `src/main/resources/db/migration`) against the PostgreSQL database.
5. An admin user is automatically seeded on startup (`admin@bank.com`).

### Core Endpoints
- **Authentication:** `POST /api/v1/auth/login` | `POST /api/v1/auth/register`
- **Transactions:** `POST /api/v1/transactions/transfer` | `GET /api/v1/transactions/me`
- **KYC (Admin):** `GET /api/v1/kyc/pending` | `PATCH /api/v1/kyc/{docId}/approve`

## 🛡 System Resilience
- Configured persistent Docker volumes (`postgres_data`) to ensure no data loss during container restarts.
- Strict entity serialization (`java.io.Serializable`) ensuring safe object caching within Redis.
- Graceful error handling and standardized API JSON responses.
