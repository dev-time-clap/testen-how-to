# Code Review: BookEntity.java

## Executive Summary

The `BookEntity` class demonstrates a well-structured approach to managing bidirectional JPA entity relationships with manual reference control. The implementation shows good understanding of Lombok annotations and builder patterns. However, there are several critical issues that need to be addressed.

---

## Critical Issues

### 1. **Builder Pattern Inconsistency with Setters**

The class uses `@Setter(AccessLevel.NONE)` on all relationship fields but provides custom setter methods. This creates a problem with the Lombok-generated builder:

```java
// The builder will try to call these setters:
BookEntity.builder()
    .withAdditionalData(data)  // ❌ No such method exists!
    .withCustomer(customer)
    .build();
```

**Problem:** Since Lombok generates private setters for fields with `@Setter(AccessLevel.NONE)`, the builder's generated methods will be **private** and inaccessible. The `@Builder(setterPrefix = "with", toBuilder = true)` configuration cannot access these private setter methods.

**Evidence:**
- All relationship fields have `@Setter(AccessLevel.NONE)`
- The custom setter methods (`setAdditionalData`, `setCustomer`, etc.) are NOT used by the builder
- The builder only uses the constructor which directly assigns to fields

**Impact:** Users of this entity cannot use the builder to set relationships. They must:
1. Use `@Builder` for basic fields only, then manually call setters
2. Or use the all-args constructor (which bypasses business logic in custom setters)

### 2. **Circular Reference Risk in `AdditionalBookDataEntity.setBook()`**

The `setAdditionalData()` and `AdditionalBookDataEntity.setBook()` methods create a circular dependency:

```java
// BookEntity.java
public void setAdditionalData(AdditionalBookDataEntity additionalData) {
    // ...
    if (additionalData != null) {
        additionalData.setBook(this);  // ← Calls back to AdditionalBookDataEntity
    }
}

// AdditionalBookDataEntity.java  
public void setBook(BookEntity book) {
    this.book = book;
    if (book != null) {
        book.setAdditionalData(this);  // ← Back to BookEntity!
    }
}
```

**Problem:** When both sides call each other's setters, there's potential for infinite recursion or duplicate work if the equality check fails. The equality checks prevent infinite loops, but the pattern is fragile.

### 3. **Missing `removeAuthor()` Method**

You have `addBookPublisher()`, `removeBookPublisher()`, `addAuthor()`, but no `removeAuthor()`. For consistency and to properly manage bidirectional many-to-many relationships, a `removeAuthor()` method should be added:

```java
public void removeAuthor(final AuthorEntity author) {
    Objects.requireNonNull(author);
    
    if (!this.authors.contains(author)) {
        log.debug("The author {} of the book {} does not exist.", author, this);
        return;
    }
    
    this.authors.remove(author);
    
    // Remove inverse link
    if (author.getBooks().contains(this)) {
        author.getBooks().remove(this);
    }
}
```

### 4. **Collection Modification Bypass**

The `@Setter(AccessLevel.NONE)` on collection fields combined with public getters allows direct collection modification:

```java
// This bypasses all business logic!
bookEntity.getBookPublishers().add(newPublisher);  // No inverse link set!
bookEntity.getAuthors().remove(existingAuthor);    // No inverse removal!
```

**Recommendation:** Consider returning unmodifiable collections or using `@Access(AccessType.FIELD)` to prevent direct field access.

---

## Structural Issues

### 5. **Inconsistent Builder Configuration**

The builder uses `@Builder(setterPrefix = "with", toBuilder = true)` but the fields have private setters. This should be:

**Option A:** Remove `@Setter(AccessLevel.NONE)` from relationship fields and let Lombok generate setters (but then you lose custom business logic)

**Option B:** Keep current approach, but add a custom builder or use `@Builder(builderMethodName = "builder")` with manual implementation

**Recommended Solution:**

```java
// Option 1: Use @Builder.Default for collections
@OneToMany(mappedBy = "book")
@ToString.Exclude
@Builder.Default  // ← Add this
private Set<BookPublisherEntity> bookPublishers = new HashSet<>();
```

### 6. **Missing `clear` Methods**

There's no way to clear all relationships at once:

```java
// Should support:
book.clearBookPublishers();
book.clearAuthors();
```

---

## Best Practices Applied

✅ Proper use of `@ToString.Exclude` on collections to prevent infinite recursion  
✅ Consistent null checking with `Objects.requireNonNull()`  
✅ Equality checks to prevent circular setter calls  
✅ Logging for debugging relationship operations  
✅ Proper use of `AccessLevel.NONE` to enforce business logic  
✅ Bidirectional link management  

---

## Recommendations

1. **Fix Builder Issue:** Either remove `@Setter(AccessLevel.NONE)` or implement a custom builder that uses your custom setter methods
2. **Add `removeAuthor()` method** for consistency
3. **Consider defensive collection handling** (return unmodifiable views)
4. **Add `clear*` methods** for bulk relationship removal
5. **Document the builder pattern usage** in class javadoc
6. **Consider using JPA callbacks** (`@PreRemove`, `@PostLoad`) for complex relationship cleanup

---

## Conclusion

The entity demonstrates solid understanding of bidirectional relationship management. The main issues are:
1. Builder pattern not working with custom setters (critical)
2. Missing symmetry in `removeAuthor()` method
3. Collection modification bypass vulnerability

**Severity:** Critical issues should be fixed before production use.
