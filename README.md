# 📚 Spring Data JPA — Bookstore API

A RESTful API built with **Spring Boot** and **Spring Data JPA**, designed to explore and practice the most commonly used JPA annotations and patterns in a real-world-like scenario.

---

## 🎯 Project Goal

The main focus of this project is to practise and understand **Spring Data JPA annotations**, covering entity relationships, repository patterns, and best practices for building a clean and maintainable data layer.

---

## 🛠️ Tech Stack

| Technology | Version |
|---|---|
| Java | 25 |
| Spring Boot | 4.x |
| Spring Data JPA | 4.x |
| Hibernate | (via Spring Data JPA) |
| Lombok | Latest |
| Maven | 3.x |

---

## 📁 Project Structure

```
com.bookstore.jpa
├── controllers
│   └── BookController.java
├── dtos
│   ├── BookRequestDto.java
│   └── BookResponseDto.java
├── entities
│   ├── Author.java
│   ├── Book.java
│   ├── Publisher.java
│   └── Review.java
├── mappers
│   └── BookMapper.java
├── repositories
│   ├── AuthorRepository.java
│   ├── BookRepository.java
│   ├── PublisherRepository.java
│   └── ReviewRepository.java
└── services
    ├── IBookService.java
    └── BookService.java
```

---

## 🗂️ Entity Relationships

```
Publisher (1) ──────── (N) Book (1) ──────── (1) Review
                             │
                           (N:N)
                             │
                           Author
```

| Relationship | Entities | Type |
|---|---|---|
| Publisher → Book | One publisher has many books | `@OneToMany` / `@ManyToOne` |
| Book ↔ Author | A book has many authors, an author writes many books | `@ManyToMany` |
| Book → Review | One book has one review | `@OneToOne` |

---

## 🏷️ Spring Data JPA Annotations

This project was specifically designed to practise the following annotations:

### Entity & Table Mapping

| Annotation | Usage in this project |
|---|---|
| `@Entity` | Marks `Author`, `Book`, `Publisher`, `Review` as JPA entities |
| `@Table(name = "...")` | Maps each entity to its corresponding database table |
| `@Id` | Marks the primary key field (`UUID id`) in all entities |
| `@GeneratedValue(strategy = GenerationType.AUTO)` | Auto-generates UUID primary keys |
| `@Column(nullable = false, unique = true)` | Adds constraints directly on the column (e.g. title, name) |
| `@Serial` | Marks the `serialVersionUID` field in `Serializable` entities |

### Relationship Mapping

| Annotation | Usage in this project |
|---|---|
| `@OneToMany(mappedBy = "publisher")` | `Publisher` → collection of `Book` (inverse side) |
| `@ManyToOne` | `Book` → single `Publisher` (owning side) |
| `@JoinColumn(name = "publisher_id")` | Defines the foreign key column in the `BOOK` table |
| `@ManyToMany` | `Book` ↔ `Author` bidirectional relationship |
| `@JoinTable(name = "book_author", ...)` | Creates the auxiliary join table with `book_id` and `author_id` |
| `@OneToOne(mappedBy = "book", cascade = CascadeType.ALL)` | `Book` → `Review` (inverse side, cascades all operations) |
| `@OneToOne` + `@JoinColumn(name = "book_id")` | `Review` → `Book` (owning side, holds the foreign key) |

### Fetch & Cascade

| Annotation / Option | Usage in this project |
|---|---|
| `fetch = FetchType.LAZY` | Applied to collection relationships to avoid unnecessary data loading |
| `cascade = CascadeType.ALL` | On `Book.review` — persisting/deleting a book cascades to its review |

### Jackson (Serialization Control)

| Annotation | Usage in this project |
|---|---|
| `@JsonProperty(access = WRITE_ONLY)` | Applied to inverse sides (`Publisher.books`, `Author.books`, `Review.book`) to prevent circular references during JSON serialisation |

### Spring Data JPA — Repository

| Feature | Usage in this project |
|---|---|
| `JpaRepository<Entity, ID>` | All repositories extend this, providing full CRUD out of the box |
| Derived query method | `findBookByTitle(String title)` — Spring generates the query automatically from the method name |
| `@Query` (JPQL) | `findBooksByPublisherId` — custom query using JPQL for flexibility |
| `@Param` | Binds the method parameter to the named parameter in the `@Query` |

### Spring — Service & Transaction

| Annotation | Usage in this project |
|---|---|
| `@Service` | Marks `BookService` as a Spring-managed service component |
| `@Transactional` | Applied to `saveBook` and `deleteBook` to ensure atomicity |
| `@Component` | Marks `BookMapper` as a Spring-managed bean |

---

## 🔗 API Endpoints

Base URL: `/bookstore/books`

| Method | Endpoint | Description | Status |
|---|---|---|---|
| `GET` | `/bookstore/books` | Returns all books | `200 OK` |
| `POST` | `/bookstore/books` | Creates a new book | `201 Created` |
| `DELETE` | `/bookstore/books/{id}` | Deletes a book by ID | `204 No Content` |

### POST — Request Body Example

```json
{
  "title": "Clean Code",
  "publisherId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "authorsId": [
    "7c9e6679-7425-40de-944b-e07fc1f90ae7"
  ],
  "reviewComment": "A must-read for every developer."
}
```

### GET — Response Body Example

```json
[
  {
    "id": "1a2b3c4d-...",
    "title": "Clean Code",
    "publisherName": "Prentice Hall",
    "authorNames": ["Robert C. Martin"],
    "reviewComment": "A must-read for every developer."
  }
]
```

---

## 🏛️ Architecture Overview

This project follows a classic **layered architecture**:

```
Request
   │
   ▼
BookController          ← Receives HTTP requests, delegates to service
   │
   ▼
IBookService            ← Interface (abstraction layer)
BookService             ← Business logic, uses repositories and mapper
   │
   ├──► BookRepository       ← Data access (Spring Data JPA)
   ├──► AuthorRepository
   ├──► PublisherRepository
   │
   ▼
BookMapper              ← Maps between DTOs and Entities
   │
   ├──► BookRequestDto   ← Incoming data (validated with @Valid)
   └──► BookResponseDto  ← Outgoing data (controls what is exposed)
```

---

## ✅ Best Practices Applied

- **Constructor injection** over `@Autowired`
- **Interface + implementation** pattern for services (`IBookService` / `BookService`)
- **DTOs** to decouple the API layer from the persistence layer
- **Request/Response DTO separation** (`BookRequestDto` / `BookResponseDto`)
- **Manual mapper** (`BookMapper`) for full control over entity ↔ DTO conversion
- **`record`** used for DTOs — immutable and concise by nature
- **`@Valid`** on controller request body for input validation
- **`orElseThrow`** instead of `.get()` on Optional — safe and descriptive
- **`204 No Content`** on DELETE instead of returning a message string
- **`@Transactional`** on write operations to ensure data consistency

---

## ▶️ How to Run

```bash
# Clone the repository
git clone https://github.com/JoelBrunoFerreira/Spring_Data_JPA.git

# Navigate to the project folder
cd Spring_Data_JPA

# Run with Maven
./mvnw spring-boot:run
```

> Make sure your `application.properties` is configured with the correct datasource settings.

---
