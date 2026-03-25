<h1 align="center">✈️ Flowmatic — Backend</h1>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Security-JWT-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/Claude_API-Anthropic-D97706?style=for-the-badge&logo=anthropic&logoColor=white" />
</p>

---

## 📖 About the Project

**Flowmatic** is a REST API for a B2B SaaS platform that helps travel agencies create, manage and send professional travel quotes. It handles multi-agency authentication, quote lifecycle management, AI-powered PDF extraction, and image upload.

The project was developed as a final bootcamp project following a professional workflow with **Spring Boot 3**, **Spring Security + JWT**, **layered architecture (Controller → Service → Repository)**, **MapStruct** for DTO mapping, and unit testing with **Mockito**.

The API is consumed by a React frontend ([see Frontend readme](./readme_front.md)).

### 🎯 Project Goals

- Build a complete REST API with Spring Boot 3
- Implement multi-agency data isolation with `agency_id` filtering
- Secure endpoints with Spring Security + JWT and role-based access (ADMIN / EMPLOYEE)
- Integrate the Claude AI API (Anthropic) for PDF travel itinerary extraction
- Handle image uploads via Cloudinary
- Apply layered architecture with interfaces and MapStruct mappers
- Write unit tests with JUnit and Mockito

---

## ✨ Main Features

- 🔐 **JWT Authentication** — register, login, stateless token-based sessions
- 👥 **Role-based access control** — ADMIN and EMPLOYEE roles with protected endpoints
- 📝 **Quote CRUD** — create, list, update, delete and change status on travel quotes
- 🤖 **PDF extraction** — upload a PDF and extract structured quote data via the Claude AI API (Sonnet 4.6)
- 🔄 **Quote status lifecycle** — PENDING → SENT → SIGNED → PAID / CANCELLED
- 👤 **Employee management** — ADMIN can create and manage agency employees
- 🏢 **Agency settings** — logo (Cloudinary), brand colors, terms & conditions
- 🖼️ **Image search** — Unsplash integration for cover images
- ⚠️ **Global exception handling** — consistent error responses across all endpoints
- 🏗️ **Multi-agency isolation** — all data is scoped by `agency_id`

---

## 🛠️ Technologies

| Technology             | Use                                    |
| ---------------------- | -------------------------------------- |
| Java 21                | Primary language                       |
| Spring Boot 3.5        | Backend framework                      |
| Spring Security        | Authentication & authorization         |
| Spring Data JPA        | ORM with Hibernate                     |
| PostgreSQL             | Relational database                    |
| Maven                  | Dependency management and build        |
| Lombok                 | Boilerplate reduction                  |
| MapStruct 1.5          | Entity ↔ DTO mapping                   |
| java-jwt 4.5           | JWT token creation and validation      |
| Cloudinary             | Image upload and CDN                   |
| Claude API (Anthropic) | AI-powered PDF extraction (Sonnet 4.6) |
| Mockito + JUnit        | Unit testing                           |
| spring-dotenv          | Environment variables from `.env`      |

---

## 📁 Project Structure

<details>
<summary><strong>Click to expand full structure</strong></summary>

```
src/main/java/com/flowmatic/flowmatic_back/
│
├── config/
│   └── CorsConfig.java                  → CORS (allows localhost:5173)
│
├── controller/                          # REST controllers
│   ├── AuthController.java                → /api/auth
│   ├── QuoteController.java               → /api/quotes
│   ├── UserController.java                → /api/users
│   ├── AgencyController.java              → /api/agency
│   ├── UploadController.java              → /api/uploads
│   └── ImageSearchController.java         → /api/images/search
│
├── dto/
│   ├── request/
│   │   ├── auth/      (RegisterRequest, LoginRequest)
│   │   ├── quote/     (QuoteCreateRequest, QuoteUpdateRequest, QuoteDayRequest,
│   │   │               QuoteInclusionRequest, PaymentConditionRequest,
│   │   │               QuoteSupplementRequest, QuoteAccommodationRequest,
│   │   │               StatusUpdateRequest)
│   │   ├── user/      (UserCreateRequest, UserUpdateRequest)
│   │   └── agency/    (AgencyUpdateRequest)
│   └── response/
│       ├── auth/      (AuthResponse)
│       ├── quote/     (QuoteResponse, QuoteListResponse, ExtractionResponse,
│       │               QuoteDayResponse, QuoteInclusionResponse,
│       │               PaymentConditionResponse, QuoteSupplementResponse,
│       │               QuoteAccommodationResponse)
│       ├── user/      (UserResponse)
│       ├── agency/    (AgencyResponse)
│       └── image/     (ImageResultResponse)
│
├── entity/                              # JPA entities (8 tables)
│   ├── Agency.java
│   ├── User.java
│   ├── Quote.java
│   ├── QuoteDay.java
│   ├── QuoteInclusion.java
│   ├── PaymentCondition.java
│   ├── QuoteSupplement.java
│   ├── QuoteAccommodation.java
│   └── enums/
│       ├── Role.java          → ADMIN, EMPLOYEE
│       └── QuoteStatus.java   → PENDING, SENT, SIGNED, PAID, CANCELLED
│
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java     → 404
│   ├── UnauthorizedException.java         → 403
│   ├── BadRequestException.java           → 400
│   ├── DuplicateResourceException.java    → 409
│   ├── InvalidStatusTransitionException.java
│   ├── ExtractionFailedException.java     → 502
│   └── CloudinaryUploadException.java     → 502
│
├── mapper/                              # MapStruct (entity ↔ DTO)
│   ├── QuoteMapper.java
│   ├── UserMapper.java
│   └── AgencyMapper.java
│
├── repository/                          # Spring Data JPA repositories
│   ├── AgencyRepository.java
│   ├── UserRepository.java
│   ├── QuoteRepository.java
│   ├── QuoteDayRepository.java
│   ├── QuoteInclusionRepository.java
│   ├── PaymentConditionRepository.java
│   ├── QuoteSupplementRepository.java
│   └── QuoteAccommodationRepository.java
│
├── security/
│   ├── SecurityConfig.java              → Route protection rules
│   ├── CustomAuthenticationManager.java
│   ├── UserDetail.java                  → Custom UserDetails
│   └── filter/
│       ├── JWTAuthenticationFilter.java → POST /api/auth/login
│       └── JWTAuthorizationFilter.java  → Validates Bearer token on every request
│
└── service/
    ├── AuthService.java + AuthServiceImpl.java
    ├── QuoteService.java + QuoteServiceImpl.java
    ├── UserService.java + UserServiceImpl.java
    ├── AgencyService.java + AgencyServiceImpl.java
    ├── PdfExtractionService.java        → Calls Claude API
    ├── CloudinaryService.java           → Handles image uploads
    └── ImageSearchService.java          → Unsplash search
```

> Architecture follows the **Controller → Service → Repository** pattern with interfaces to decouple business logic from implementation.

</details>

---

## 🚀 Installation & Setup

### Prerequisites

- **Java 21** or higher
- **Maven 3**
- **PostgreSQL** running on port `5432`
- A **Cloudinary** account (free tier)
- A **Claude API key** (Anthropic)
- An **Unsplash** access key

### Steps

1. **Clone the repository**

```bash
git clone <repository-url>
cd flowmatic-back
```

2. **Create the database**

```sql
CREATE DATABASE flowmatic;
```

3. **Configure environment variables**

```bash
cp .env.example .env
```

```env
DB_USERNAME=your_postgres_username
DB_PASSWORD=your_postgres_password
JWT_SECRET=a_random_secret_string_at_least_32_chars
JWT_EXPIRATION=86400000
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
CLAUDE_API_KEY=your_anthropic_api_key
UNSPLASH_ACCESS_KEY=your_unsplash_access_key
```

4. **Start the application**

```bash
mvn spring-boot:run
```

Tables are created automatically on startup via Hibernate (`ddl-auto=update`).
API available at `http://localhost:8080`.

---

## 🗄️ Data Model

<details>
<summary><strong>Click to expand all tables</strong></summary>

### Agency

| Field              | Type      | Description         |
| ------------------ | --------- | ------------------- |
| id                 | BIGINT PK | Auto-generated      |
| name               | VARCHAR   | Agency name         |
| logoUrl            | VARCHAR   | Cloudinary URL      |
| primaryColor       | VARCHAR   | Hex color (#RRGGBB) |
| secondaryColor     | VARCHAR   | Hex color (#RRGGBB) |
| termsAndConditions | TEXT      | General terms (T&C) |
| createdAt          | TIMESTAMP | Auto-generated      |

### User

| Field     | Type      | Description                         |
| --------- | --------- | ----------------------------------- |
| id        | BIGINT PK | Auto-generated                      |
| agencyId  | FK        | → Agency                            |
| email     | VARCHAR   | Unique login email                  |
| password  | VARCHAR   | BCrypt hashed                       |
| firstName | VARCHAR   | First name                          |
| lastName  | VARCHAR   | Last name                           |
| roles     | ENUM[]    | ADMIN, EMPLOYEE (ElementCollection) |

### Quote

| Field            | Type      | Description                            |
| ---------------- | --------- | -------------------------------------- |
| id               | BIGINT PK | Auto-generated                         |
| agencyId         | FK        | → Agency (multi-tenancy filter)        |
| createdBy        | FK        | → User                                 |
| referenceNumber  | VARCHAR   | Unique ref (e.g. FLW-2025-001)         |
| title            | VARCHAR   | Quote title                            |
| clientName       | VARCHAR   | Client name                            |
| clientEmail      | VARCHAR   | Client email                           |
| destination      | VARCHAR   | Travel destination                     |
| startDate        | DATE      | Departure date                         |
| endDate          | DATE      | Return date                            |
| participantCount | INT       | Number of travelers                    |
| pricePerPerson   | DECIMAL   | Price per person                       |
| currency         | VARCHAR   | ISO currency code (default: EUR)       |
| status           | ENUM      | PENDING, SENT, SIGNED, PAID, CANCELLED |
| coverImageUrl    | VARCHAR   | Cover image URL                        |
| travelWishes     | TEXT      | Client travel preferences              |

### user_roles (join table — `@ElementCollection` on User)

| Field   | Type    | Description                          |
| ------- | ------- | ------------------------------------ |
| user_id | BIGINT  | FK → users                           |
| role    | VARCHAR | ADMIN or EMPLOYEE (stored as string) |

> A user can have both roles simultaneously (e.g. the agency founder is both ADMIN and EMPLOYEE). Hibernate generates this table automatically from the `roles` `@ElementCollection` on the `User` entity.

### Child Tables (CASCADE from Quote)

| Table              | Key fields                                                           |
| ------------------ | -------------------------------------------------------------------- |
| QuoteDay           | dayNumber, date, title, description, meals, transport, accommodation |
| QuoteInclusion     | description, included (BOOLEAN)                                      |
| PaymentCondition   | description, percentage, dueDateDescription                          |
| QuoteSupplement    | description, pricePerPerson, totalPrice                              |
| QuoteAccommodation | name, location, rating, description, displayOrder                    |

</details>

---

## 🔗 API Endpoints

<details>
<summary><strong>Click to expand all endpoints</strong></summary>

### 🔓 Auth — `/api/auth` (Public)

| Method | Endpoint             | Description                   |
| ------ | -------------------- | ----------------------------- |
| POST   | `/api/auth/register` | Register a new agency + admin |
| POST   | `/api/auth/login`    | Login — returns JWT token     |

### 📝 Quotes — `/api/quotes` (EMPLOYEE+)

| Method | Endpoint                  | Description                          |
| ------ | ------------------------- | ------------------------------------ |
| GET    | `/api/quotes`             | List all quotes for the agency       |
| GET    | `/api/quotes/{id}`        | Get full quote with all details      |
| POST   | `/api/quotes`             | Create a new quote                   |
| PUT    | `/api/quotes/{id}`        | Update quote (replaces all children) |
| DELETE | `/api/quotes/{id}`        | Delete quote                         |
| PATCH  | `/api/quotes/{id}/status` | Update status `{status: "SENT"}`     |
| POST   | `/api/quotes/extract`     | Upload PDF → Claude AI → JSON        |

### 👤 Users — `/api/users` (ADMIN only)

| Method | Endpoint          | Description           |
| ------ | ----------------- | --------------------- |
| GET    | `/api/users`      | List agency employees |
| POST   | `/api/users`      | Create a new employee |
| PUT    | `/api/users/{id}` | Update employee       |
| DELETE | `/api/users/{id}` | Delete employee       |

### 🏢 Agency — `/api/agency` (ADMIN only)

| Method | Endpoint      | Description                             |
| ------ | ------------- | --------------------------------------- |
| GET    | `/api/agency` | Get agency settings (logo, colors, T&C) |
| PUT    | `/api/agency` | Update agency settings                  |

### 🖼️ Uploads — `/api/uploads` (Authenticated)

| Method | Endpoint             | Description                   |
| ------ | -------------------- | ----------------------------- |
| POST   | `/api/uploads/image` | Upload image → Cloudinary URL |

### 🔍 Image Search — `/api/images` (EMPLOYEE+)

| Method | Endpoint                       | Description                    |
| ------ | ------------------------------ | ------------------------------ |
| GET    | `/api/images/search?query=...` | Search images via Unsplash API |

</details>

---

## 🤖 AI-Powered PDF Extraction

The `POST /api/quotes/extract` endpoint accepts a multipart PDF and processes it with **Claude API (claude-sonnet-4-6)**. The prompt lives in `src/main/resources/prompts/extraction-prompt.txt` and instructs Claude to return a structured JSON matching the quote schema (days, inclusions, payment conditions, etc.).

The extracted JSON is returned to the frontend where the user reviews and validates before saving — no ghost quotes are ever stored.

---

## 👩‍💻 Team

| Developer                   | GitHub                                                   | LinkedIn                                                        |
| --------------------------- | -------------------------------------------------------- | --------------------------------------------------------------- |
| **Marie-Charlotte Doulcet** | [@Charlottedoulcet](https://github.com/Charlottedoulcet) | [LinkedIn](https://www.linkedin.com/in/marie-charlottedoulcet/) |

> 💙 Project developed during the **FemCoders Bootcamp P8 — March 2026**

---

## 🌱 Possible Future Improvements

- 📧 **Email notifications** — when an ADMIN creates an employee account, the employee receives an email with a setup link to choose their own password, manage their profile (photo, preferences), and a "forgot password" flow
- 👥 **Customer management** — a customer book per agency (name, email, history) with the ability to send a quote directly by email from the app
- 🔗 **Client-facing portal** — shareable public link for clients to view and accept a quote (no login required)
- 🍪 **Secure token storage** — move JWT from localStorage to HTTP-only cookies to prevent XSS exposure
- 🧪 **Expand unit and integration test coverage** — services, repositories, and controller layers
