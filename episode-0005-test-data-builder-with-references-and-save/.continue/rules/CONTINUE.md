# Demo Bibliothek - Project Guide

## Project Overview

**Demo Bibliothek** (German for "Demo Library") is a Spring Boot application demonstrating domain-driven design patterns with test data builders that support references and database persistence. This project was created as part of a tutorial series (episode-0005) focused on building comprehensive test data builder frameworks.

### Key Technologies

- **Java 25**
- **Spring Boot 4.0.1** - Modern Java application framework
- **Spring Data JPA** - Object-relational mapping
- **H2 Database** - In-memory database for development/testing
- **Lombok** - Code generation annotations
- **Maven** - Build and dependency management

### High-Level Architecture

The application follows a layered architecture with clear separation of concerns:

```
┌─────────────────────────────────────────────────────┐
│              REST API Layer (Controller)            │
│  - BookRestController (HTTP endpoints)             │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│          Business Logic / Command Layer             │
│  - UseCaseHandler (Event processing)               │
│  - Event Commands & Domain Events                  │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│           Business Object Layer (BO)                │
│  - Book, Author, Publisher, Customer entities      │
│  - Business rule enforcement                       │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│          Persistence Layer (JPA/Hibernate)          │
│  - Entity classes with JPA annotations             │
│  - Repository interfaces                           │
└─────────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────────┐
│              Test Data Builder Framework            │
│  - TestDataBuilder interface                       │
│  - SaveContext & RecursionGuard utilities          │
│  - TransactionHelper for database operations       │
└─────────────────────────────────────────────────────┘
```

---

## Getting Started

### Prerequisites

- **Java Development Kit (JDK) 25** or later
- **Maven 3.8+**
- **IDE with Java support** (IntelliJ IDEA, Eclipse, VS Code)

### Installation

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd episode-0005-test-data-builder-with-references-and-save
   ```

2. Build the project:
   ```bash
   mvn clean install
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

### Basic Usage

The application exposes two main REST endpoints:

1. **Register a Book** (`POST /api/books/registration`)
   - Registers a new book with its author and publisher
   
2. **Loan a Book** (`POST /api/books/loan`)
   - Loans an existing book to a registered customer

### Access Points

- **API Documentation**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **H2 Database Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `example`
  - Password: `example`

### Running Tests

```bash
mvn test
```

For integration tests:
```bash
mvn verify
```

---

## Project Structure

### Directory Layout

```
src/
├── main/
│   └── java/de/devtime/examples/library/
│       ├── DemoBibliothekApplication.java           # Main application class
│       ├── context/                                 # Application context utilities
│       │   ├── ApplicationContextProvider.java      # Spring context access
│       │   └── AbstractManualAutowiredBean.java     # Manual dependency injection base
│       ├── api/                                     # REST API layer
│       │   ├── impl/
│       │   │   └── BookRestController.java          # REST endpoints for books
│       │   ├── contract/                            # DTOs and constants
│       │   │   ├── book/                            # Book-related DTOs
│       │   │   ├── author/                          # Author-related DTOs
│       │   │   ├── publisher/                       # Publisher-related DTOs
│       │   │   └── EndpointConstants.java           # API endpoint definitions
│       │   └── AbstractPersistableDto.java          # Base class for persistable DTOs
│       ├── businesslogic/                           # Business logic layer
│       │   ├── handler/
│       │   │   └── UseCaseHandler.java              # Event processing and use cases
│       │   └── object/                              # Business objects (BO)
│       │       ├── AbstractBusinessObject.java      # Base class for business objects
│       │       ├── Book.java                        # Book business logic
│       │       ├── Author.java                      # Author business logic
│       │       ├── Publisher.java                   # Publisher business logic
│       │       ├── Customer.java                    # Customer business logic
│       │       └── Factory classes (BookFactory, etc.)
│       ├── event/                                   # Event-driven architecture
│       │   ├── AbstractEvent.java                   # Base class for all events
│       │   ├── command/                             # Command events (input)
│       │   │   ├── RegisterBookCommandEvent.java    # Book registration command
│       │   │   └── LoanBookCommandEvent.java        # Book loaning command
│       │   └── domain/                              # Domain events (output)
│       │       ├── BookRegisteredDomainEvent.java
│       │       ├── AuthorRegisteredDomainEvent.java
│       │       └── PublisherRegisteredDomainEvent.java
│       └── persistence/                             # Data persistence layer
│           ├── entity/                              # JPA entities
│           │   ├── AbstractEntity.java              # Base class for entities with UUID
│           │   ├── BookEntity.java                  # Book database entity
│           │   ├── AuthorEntity.java                # Author database entity
│           │   ├── PublisherEntity.java             # Publisher database entity
│           │   ├── CustomerEntity.java              # Customer database entity
│           │   └── AdditionalBookDataEntity.java    # Extended book information
│           ├── repository/                          # JPA repositories
│           │   ├── BookRepository.java
│           │   ├── AuthorRepository.java
│           │   ├── PublisherRepository.java
│           │   ├── CustomerRepository.java
│           │   └── AdditionalBookDataRepository.java
│           └── util/
│               └── TransactionHelper.java           # Transaction management utility
└── test/
    └── java/de/devtime/examples/library/test/builder/
        ├── TestDataBuilder.java                     # Core builder interface
        ├── TestDataBuilderWithSaveSupport.java      # Builder with DB save support
        ├── SaveContext.java                         # Tracks saved entities during build
        └── RecursionGuard.java                      # Prevents circular dependencies
```

### Key Configuration Files

- `pom.xml`: Maven project configuration with Spring Boot dependencies
- `src/main/resources/application.yaml`: Application configuration (database, H2 console)
- `src/main/resources/log4j2.xml`: Logging configuration
- `src/test/resources/log4j2-test.xml`: Test-specific logging configuration

---

## Development Workflow

### Coding Standards

1. **Naming Conventions**:
   - Classes: PascalCase (e.g., `BookEntity`, `UseCaseHandler`)
   - Methods/Variables: camelCase (e.g., `register()`, `bookRepository`)
   - Constants: UPPER_SNAKE_CASE (e.g., `MAX_LOAN_DURATION_DAYS`)

2. **Builder Pattern**:
   - All entities and DTOs use Lombok's `@Builder(setterPrefix = "with")`
   - Example: `BookEntity.builder().withIsbn("123-456").withTitle("Example").build()`

3. **Transaction Management**:
   - Use `TransactionHelper` for explicit transaction control
   - Domain events fire after commit using `txHelper.registerTaskAfterCommit()`

4. **Testing Approach**:
   - Unit tests with JUnit and Mockito
   - Test data builders support both in-memory builds and database persistence

### Build and Deployment Process

1. **Local Development**:
   ```bash
   mvn spring-boot:run
   ```

2. **Build for Production**:
   ```bash
   mvn clean package
   java -jar target/episode-0005-test-data-builder-with-references-and-save-0.0.1-SNAPSHOT.jar
   ```

3. **Run Tests**:
   ```bash
   mvn test          # Unit tests only
   mvn verify        # Include integration tests
   ```

### Contribution Guidelines

1. Follow existing code style and patterns
2. Use test data builders for creating test data
3. Add domain events when business rules change entity state
4. Ensure all public methods are properly documented
5. Run `mvn clean install` before committing changes

---

## Key Concepts

### Domain-Driven Design Elements

1. **Entities**: Core domain objects with identity (`BookEntity`, `AuthorEntity`)
2. **Value Objects**: Immutable objects without identity (DTOs, Commands, Events)
3. **Domain Events**: Notifications about significant business occurrences
4. **Business Objects**: Encapsulate business rules and operations

### Event-Driven Architecture

The application uses Spring's event listener mechanism:

1. **Command Events** (`RegisterBookCommandEvent`, `LoanBookCommandEvent`)
   - Input events triggered by REST controllers
   - Processed by `UseCaseHandler`

2. **Domain Events** (`BookRegisteredDomainEvent`, etc.)
   - Output events fired after successful operations
   - Used for decoupled business logic extensions

### Test Data Builder Pattern

A sophisticated framework for creating test data with these features:

1. **Core Interface**: `TestDataBuilder<E>` with methods:
   - `buildOnlyThis()`: Build entity without references
   - `buildWithReferences()`: Build entity with all references

2. **Save Support**: `TestDataBuilderWithSaveSupport<E, R>` adds:
   - Database persistence during build
   - Reference tracking across builders
   - Circular dependency prevention

3. **Key Components**:
   - `SaveContext`: Tracks entities already saved during a test
   - `RecursionGuard`: Prevents infinite recursion in bidirectional relationships
   - `TransactionHelper`: Manages database transactions for tests

### Transaction Management

The `TransactionHelper` class provides:

1. **Explicit Transaction Control**:
   ```java
   txHelper.executeInTx(() -> {
       // Business logic here
   });
   ```

2. **Post-Commit Hooks**:
   ```java
   txHelper.registerTaskAfterCommit(() -> {
       // Fire domain event after transaction commits
   });
   ```

3. **Flexible Naming Strategies**:
   - `uuid`: Random UUID for transaction names (default)
   - `class-and-method`: Uses caller class and method name

---

## Common Tasks

### Adding a New Entity

1. Create the entity class extending `AbstractEntity`:
   ```java
   @Entity
   @Table(name = "MyEntity")
   public class MyEntity extends AbstractEntity<MyEntity> {
       // Fields, getters, setters
   }
   ```

2. Create repository interface:
   ```java
   public interface MyEntityRepository extends JpaRepository<MyEntity, UUID> {}
   ```

3. Create business object:
   ```java
   @Component
   @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
   public class MyBusinessObject extends AbstractBusinessObject<MyBusinessObject> {
       private final TransactionHelper txHelper;
       private final MyRepository repo;
       
       // Constructor, methods
   }
   ```

4. Create test data builder:
   ```java
   public class MyEntityTestDataBuilder<B> 
       extends MyEntityBuilder<B>
       implements TestDataBuilderWithSaveSupport<MyEntity, MyRepository> {
       // Implementation
   }
   ```

### Modifying Business Logic

1. Locate the relevant business object in `businesslogic/object/`
2. Modify methods to implement new rules or behaviors
3. Ensure domain events are fired when appropriate:
   ```java
   txHelper.registerTaskAfterCommit(() -> fireDomainEvent(entity));
   ```

### Creating API Endpoints

1. Define DTOs in `api/contract/` subpackage
2. Create REST controller in `api/impl/`
3. Implement logic using event commands or direct business object calls
4. Add Swagger annotations for documentation:

```java
@Operation(summary = "Endpoint description", tags = "Tags")
@PostMapping("/path")
public void endpointMethod(@RequestBody RequestDto dto) {
    // Implementation
}
```

---

## Troubleshooting

### Common Issues

1. **H2 Console Not Accessible**:
   - Ensure `spring.h2.console.enabled=true` in `application.yaml`
   - Verify port (default: 8080)
   - Check application logs for startup errors

2. **Test Data Builder Circular Dependencies**:
   - Use `RecursionGuard.guard()` when building bidirectional relationships
   - Example: `RecursionGuard.guard(ParentBuilder.class, () -> childBuilder.build())`

3. **Transaction Not Committing Changes**:
   - Ensure methods are annotated with `@Transactional`
   - Check `TransactionHelper` is being used for complex transaction scenarios

4. **Entity Persistence Issues**:
   - Verify entity has proper JPA annotations
   - Check repository extends `JpaRepository`
   - Confirm transaction boundaries are correct

### Debugging Tips

1. Enable detailed logging in `application.yaml`:
   ```yaml
   logging:
     level:
       de.devtime.examples.library: DEBUG
       org.springframework.transaction: DEBUG
   ```

2. Use H2 console to inspect database state during tests

3. Add breakpoint in `TransactionHelper.getTxName()` to verify transaction naming strategy

4. Review `RecursionGuard` logic when builder methods don't complete as expected

---

## References

### Documentation

- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/4.0.1/reference/htmlsingle/)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Lombok Documentation](https://projectlombok.org/)
- [Maven Documentation](https://maven.apache.org/guides/index.html)

### Project-Specific Resources

- **Tutorial Series**: This project is part of a series demonstrating test data builder patterns
- **Package Structure**: See `src/main/java/de/devtime/examples/library/` for complete code examples

### Related Patterns

1. **Builder Pattern**: For fluent object construction
2. **Domain-Driven Design**: Bounded contexts, aggregates, domain events
3. **Event Sourcing**: Command and domain event separation
4. **Repository Pattern**: Data access abstraction
5. **Test Data Builder**: Flexible test data creation with references

---

*Note: This guide was automatically generated from the project codebase. For questions or corrections, please contact the development team.*
