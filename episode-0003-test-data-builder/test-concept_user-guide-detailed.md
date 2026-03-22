# Test Data Builder Pattern - A User Guide for Junior Developers

## 1) Introduction: Why This Matters

In software testing, we often need to create complex test objects with many fields. For our Spring Boot library application, creating `BookEntity` objects with all their dependencies can be tedious and error-prone.

This guide explains the "Test Data Builder" pattern that helps us create clean, readable, and maintainable test data without repeating the same setup code over and over.

## 2) The Problem We're Solving

### Before: Manual Object Creation (The Hard Way)

Imagine you want to test a book loan feature. You need a `BookEntity` with:

- A UUID for the ID (not null)
- A version number (starting at 0)
- Valid ISBN
- Book title
- isOnLoan flag set to false

Without a builder pattern, your test might look like this:

```java
@Test
void loanBookTest() {
    // Manual setup - verbose and repetitive!
    BookEntity book = new BookEntity();
    UUID id = UUID.randomUUID(); // You have to generate it yourself
    book.setId(id);
    book.setVersion(0);
    book.setIsbn("ISBN-12345");
    book.setTitle("Test Book");
    book.setOnLoan(false);
    
    // Now you can finally test something!
    // ...
}
```

### Problems with Manual Creation:

1. **Repetitive**: You have to write the same setup code in every test
2. **Brittle**: If a new field is required, you break all tests
3. **Error-prone**: Easy to forget fields or set wrong values
4. **Unclear intent**: Hard to see what's important vs. just boilerplate

## 3) Our Solution: The Test Data Builder Pattern

We use two complementary patterns together:

1. **Lombok's `@Builder` annotation** - For flexible object construction
2. **Custom TestDataBuilder classes** - For test-specific convenience methods

Let me explain how each part works.

---

### Part A: Understanding the Base Entity (`AbstractEntity.java`)

```java
@MappedSuperclass
public abstract class AbstractEntity<E extends AbstractEntity<?>> 
    implements Persistable<UUID> {
    
  @Id
  private UUID id;
  
  @Version
  private int version;
  
  @Transient
  private boolean persisted;
  
  // ... helper methods ...
}
```

**What's happening here:**

- `@MappedSuperclass` means this class provides fields to JPA entities but isn't a table itself
- `Persistable<UUID>` is a Spring Data interface that helps track whether an object is new or already saved
- The `persisted` flag is set by `@PostLoad` and `@PostPersist` callbacks from JPA

**Key Method: `generateId()`**
```java
public E generateId() {
  setId(Generators.timeBasedEpochRandomGenerator().generate());
  return (E) this; // Returns itself for method chaining!
}
```

This method generates a UUID and returns the object so you can chain methods:
```java
BookEntity book = new BookEntity().generateId();
```

---

### Part B: The Entity Itself (`BookEntity.java`)

```java
@Entity
@Table(name = "Book")
@Builder(setterPrefix = "with")
public class BookEntity extends AbstractEntity<BookEntity> {
  @Column(name = "ISBN", nullable = false)
  private String isbn;
  
  // ... other fields ...
}
```

**Lombok's `@Builder` with custom prefix:**

- By default, Lombok generates methods like `.isbn("123")`
- With `setterPrefix = "with"`, we get methods like `.withIsbn("123")`
- This follows a fluent interface style that reads naturally

**Example usage:**
```java
BookEntity book = BookEntity.builder()
    .withIsbn("ISBN-978-3864905042")
    .withTitle("Effective Java")
    .withIsOnLoan(false)
    .build();
```

---

### Part C: The Test Data Builder Base Interface (`TestDataBuilder.java`)

```java
public interface TestDataBuilder<E> {
  E buildInternally();
  
  default E buildOnlyThis() {
    return buildInternally();
  }
}
```

**What it does:**
- Defines a contract for building test objects
- `buildInternally()` - The main method that creates and configures the object
- `buildOnlyThis()` - Optional convenience method (defaults to `buildInternally()`)

---

### Part D: Our Book Entity Builder (`BookEntityTestDataBuilder.java`)

This is where it all comes together:

```java
public class BookEntityTestDataBuilder<B extends TestDataBuilder<BookEntity>>
    extends BookEntityBuilder<B>
    implements TestDataBuilder<BookEntity> {
    
  private UUID id;
  private int version;
  private boolean useExternalId = false;
  
  public B withId(final UUID id) {
    this.id = id;
    this.useExternalId = true;
    return and(); // Returns the builder for chaining
  }
  
  @Override
  public BookEntity buildInternally() {
    BookEntity entity = build().generateId(); // Use Lombok builder + generateId()
    if (this.useExternalId) {
      entity.setId(this.id); // Override with our custom ID if provided
    }
    entity.setVersion(this.version);
    return entity;
  }
}
```

**How it works:**

1. **Extends the Lombok builder**: `extends BookEntityBuilder<B>` gives us access to all the generated methods like `.withIsbn()`
2. **Adds convenience fields**: We store test-specific values (ID, version)
3. **Custom methods with chaining**: `withId()` sets our ID and returns the builder
4. **Smart default behavior**: 
   - Calls `build().generateId()` to create the entity with a UUID
   - Allows overriding the ID if we call `withId()`
   - Sets the version (default 0 if not specified)

---

### Part E: The Test Data Provider (`BookEntityTestDataProvider.java`)

```java
public class BookEntityTestDataProvider extends BookEntityTestDataBuilder<BookEntityTestDataProvider> {
  
  public static BookEntityTestDataProvider create() {
    return new BookEntityTestDataProvider();
  }
  
  // Pre-configured scenarios for common test cases
  public BookEntityTestDataProvider bookByMorriganWithTitleLombokHowTo() {
    withIsbn("ISBN-0815");
    withTitle("Lombok - How to");
    withIsOnLoan(false);
    return and(); // Return the builder
  }
  
  // ... more methods for other books ...
}
```

**What this does:**

- Extends our base test data builder to add domain-specific helper methods
- Provides **reusable, meaningful scenarios** that make tests more readable
- The `and()` method (from `GenericBuilder`) returns the builder instance

---

## 4) Using It All Together - Examples

### Basic Usage - Creating a Book with Default Values:

```java
@Test
void simpleBookCreationTest() {
    // Create book with random UUID and default version = 0
    BookEntity book = BookEntityTestDataBuilder.<BookEntity>create()
        .withIsbn("ISBN-123")
        .withTitle("My Test Book")
        .buildOnlyThis();
    
    assertNotNull(book.getId());       // ✅ UUID is generated automatically
    assertEquals(0, book.getVersion()); // ✅ Version defaults to 0
    assertEquals("ISBN-123", book.getIsbn());
}
```

### Using Custom ID and Version:

```java
@Test
void customIdAndVersionTest() {
    UUID customId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    
    BookEntity book = BookEntityTestDataBuilder.<BookEntity>create()
        .withId(customId)          // Override auto-generated ID
        .withVersion(5)            // Set custom version (optimistic locking)
        .withIsbn("ISBN-456")
        .buildOnlyThis();
    
    assertEquals(customId, book.getId());
    assertEquals(5, book.getVersion());
}
```

### Using Test Data Provider - Pre-built Scenarios:

```java
@Test
void testWithPredefinedBook() {
    // Get a pre-configured book from our "test data provider"
    BookEntity book = BookEntityTestDataProvider.create()
        .bookByMorriganWithTitleLombokHowTo()
        .buildInternally();
    
    assertEquals("ISBN-0815", book.getIsbn());
    assertEquals("Lombok - How to", book.getTitle());
    assertFalse(book.isOnLoan());
}
```

### Chaining Multiple Operations:

```java
@Test
void complexTest() {
    // You can even create multiple related entities easily
    BookEntity book1 = BookEntityTestDataProvider.create()
        .bookByMorriganWithTitleLombokHowTo()
        .buildInternally();
        
    BookEntity book2 = BookEntityTestDataProvider.create()
        .bookByMorriganWithTitleTestingWithJUnitAndCo()
        .buildInternally();
    
    // Now test your business logic with these books
    assertTrue(book1.getId() != book2.getId()); // Different books, different IDs!
}
```

---

## 5) Comparison: Before vs. After

| Aspect | Manual Creation | Test Data Builder Pattern |
|--------|-----------------|---------------------------|
| **Code Length** | Many lines per test | 3-4 lines |
| **Maintainability** | Update every test if entity changes | Update builder once |
| **Readability** | Boilerplate obscures intent | Clear what's being tested |
| **Reusability** | Copy-paste code everywhere | Reusable builder methods |
| **Error Rate** | Easy to forget required fields | Builder handles defaults |

---

## 6) Why We Chose This Approach

### 1. **Lombok `@Builder` with Custom Prefix**
- ✅ Reduces boilerplate significantly
- ✅ Fluent, readable API (`.withX()` methods)
- ✅ Works well with inheritance hierarchies

**Alternatives Considered:**
- Manual builder class: More code to maintain
- Lombok's default prefix: Less readable (`.isbn("123")` vs `.withIsbn("123")`)
- Constructor injection: Doesn't scale well with many optional parameters

### 2. **Generic Test Data Builder Pattern**
- ✅ Separates test data creation from test logic
- ✅ Provides sensible defaults for most tests
- ✅ Allows customization when needed

**Alternatives Considered:**
- Factory methods in each test class: No reusability across test classes
- Mock objects with Mockito: Good for unit tests, not for integration tests
- Test data templates: Less flexible and harder to understand

### 3. **Separate Test Data Provider Class**
- ✅ Encapsulates domain-specific scenarios
- ✅ Makes tests self-documenting (`.bookByMorriganWithTitleLombokHowTo()` is clearer than raw values)
- ✅ Easy to add new pre-configured scenarios as your application grows

---

## 7) Best Practices & Tips

### ✅ DO:
- Use meaningful method names in TestDataProvider
- Keep default values reasonable for most tests
- Override only what's necessary in specific tests
- Document the purpose of each predefined scenario

### ❌ DON'T:
- Put business logic in test data builders
- Create too many overlapping scenarios (keep it simple)
- Access private fields or methods directly

---

## 8) Quick Reference Card

### Creating a BookEntity:

```java
// Basic - with generated UUID and default version
BookEntity book = TestDataBuilder.<BookEntity>create()
    .withIsbn("123")
    .buildOnlyThis();

// With custom ID
BookEntity book = TestDataBuilder.<BookEntity>create()
    .withId(myId)
    .withVersion(5)
    .buildOnlyThis();

// Using pre-defined scenario
BookEntity book = TestDataProvider.create()
    .bookByMorriganWithTitleLombokHowTo()
    .buildOnlyThis();
```

### Creating Multiple Related Entities:

```java
// Create 3 different books easily
List<BookEntity> books = Stream.of(
    TestDataProvider.create().bookByMorriganWithTitleLombokHowTo(),
    TestDataProvider.create().bookByMorriganWithTitleTestingWithJUnitAndCo(),
    TestDataProvider.create().bookByMorriganWithTitleSpringBootPrototyping()
).map(TestDataBuilder::buildOnlyThis)
 .toList();
```

---

## 9) Summary

The Test Data Builder pattern gives you:

1. **Less code** - No more repetitive object setup
2. **More clarity** - Tests focus on what matters, not boilerplate
3. **Better maintainability** - Changes to entities only require builder updates
4. **Reusable scenarios** - Pre-configured data for common test cases

Just remember:
- Use `buildInternally()` to get your entity
- Use pre-defined methods when they match your needs
- Override only what's different in specific tests

Happy testing! 🧪

