# Development Guide for Spring Boot Library Application

## Quick Start

This is a Spring Boot 4.0.1 application using Java 25, Maven for build management, and Lombok for boilerplate reduction.

## Project Structure Overview

```
src/
├── main/java/de/devtime/examples/library/
│   ├── persistence/    # Database entities and repositories
│   ├── businesslogic/  # Business objects that orchestrate operations
│   ├── api/            # REST API endpoints and DTOs
│   └── event/          # Domain events
└── test/java/          # Test classes using TestDataBuilder pattern
```

## Key Components

### Entity Layer (JPA)
- `AbstractEntity`: Base class for all entities with UUID ID and version fields
- `BookEntity`, `PublisherEntity`, etc.: Specific domain entities
- All use Lombok's `@Builder(setterPrefix = "with")` for fluent construction

### Test Layer (TestDataBuilder Pattern)
- `TestDataBuilder`: Interface for test object builders
- `BookEntityTestDataBuilder`: Custom builder with smart defaults and convenience methods
- `BookEntityTestDataProvider`: Pre-configured scenarios for common test cases

## Building the Project

```bash
# Build the application
mvn clean package

# Run tests
mvn test

# Start the application (H2 database for demo)
mvn spring-boot:run
```

## Development Workflow

1. **Adding new entity fields**: Update entity class + Lombok builder will automatically provide `.withNewField()` method
2. **Creating test data**: Use `TestDataBuilder.<Type>create().buildInternally()`
3. **For common scenarios**: Add methods to appropriate TestDataProvider class

## Testing Approach

We use the TestDataBuilder pattern to:
- Reduce boilerplate in tests
- Provide sensible defaults (auto-generated UUIDs, version = 0)
- Create reusable test data scenarios
- Keep tests focused on what's being tested

Example:
```java
BookEntity book = BookEntityTestDataProvider.create()
    .bookByMorriganWithTitleLombokHowTo()
    .buildInternally();
```

## Key Technologies

- **Spring Boot 4.0.1** - Application framework
- **JPA/Hibernate** - Object-relational mapping
- **H2 Database** - In-memory database for development/testing
- **Lombok** - Reduce boilerplate with `@Builder`, `@Getter/@Setter`, etc.
- **JUnit 5** - Testing framework

## Development Workflow

### Adding a new entity field:
```java
// In your Entity class:
@Column(name = "NEW_FIELD")
private String newField;

// Now you can use .withNewField() in tests and with builder pattern
```

### Creating test data with custom values:
```java
TestDataBuilder.<Entity>create()
    .withId(customUUID)
    .withVersion(5)
    .buildInternally();
```

### Adding a new pre-configured test scenario:
```java
// In TestDataProvider class:
public BookEntityTestDataProvider bookByAuthorWithTitle(String author, String title) {
    withIsbn(generateIsbn());
    withTitle(title);
    return and();
}
```

## Notes for Junior Developers

- The `and()` method returns the builder instance for method chaining
- Use `buildInternally()` to get your final entity object
- Pre-defined methods in TestDataProvider make tests more readable
- Lombok annotations reduce the need for manual getter/setter code
- The version field is used for optimistic locking (concurrency control)

## References

- Spring Boot Documentation: https://spring.io/projects/spring-boot
- Lombok Documentation: https://projectlombok.org/
- JPA/Hibernate Guide: https://hibernate.org/orm/documentation/
