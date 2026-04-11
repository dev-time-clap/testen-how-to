package de.devtime.examples.library.persistence.entity;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@Getter
@Setter

@Entity
@Table(name = "AUTHOR")
public class AuthorEntity extends AbstractEntity<AuthorEntity> {

  @Column(name = "ARTIST_NAME", nullable = false)
  private String artistName;

  @Column(name = "BIRTHDAY")
  private LocalDate birthday;

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "LAST_NAME")
  private String lastName;

  // Inverse link
  @OneToMany(mappedBy = "author")
  @ToString.Exclude
  @Setter(AccessLevel.PACKAGE)
  private Set<BookAuthorEntity> bookAuthors = new HashSet<>();

  //--------------------< Handle bi-directional relationships >--------------------

  public Set<BookAuthorEntity> getBookAuthors() {
    return Collections.unmodifiableSet(this.bookAuthors);
  }

  public void addBookAuthor(final BookAuthorEntity bookAuthor) {
    Objects.requireNonNull(bookAuthor);

    // Avoid endless loops
    if (this.bookAuthors.contains(bookAuthor)) {
      log.debug("The book-author relation {} of the author {} already exist.", bookAuthor, this);
      return;
    }

    // Apply new foreign link
    this.bookAuthors.add(bookAuthor);
  }

  public void removeBookAuthor(final BookAuthorEntity bookAuthor) {
    Objects.requireNonNull(bookAuthor);

    // Avoid endless loops
    if (!this.bookAuthors.contains(bookAuthor)) {
      log.debug("The book-author relation {} is not associated with the author {}", bookAuthor, this);
      return;
    }

    // Remove foreign link
    this.bookAuthors.remove(bookAuthor);
  }

  //--------------------< Builder-Pattern Support >--------------------

  @Builder(setterPrefix = "with", toBuilder = true)
  private AuthorEntity(
      final UUID id,
      final int version,
      final String artistName,
      final LocalDate birthday,
      final String firstName,
      final String lastName,
      @Singular final Set<BookAuthorEntity> bookAuthors) {
    // Simple fields
    super(id, version, false);
    this.artistName = artistName;
    this.birthday = birthday;
    this.firstName = firstName;
    this.lastName = lastName;

    // Referenced entities
    this.bookAuthors = bookAuthors == null ? new HashSet<>() : new HashSet<>(bookAuthors);
  }

  public static class AuthorEntityBuilder<B> implements GenericBuilder<B> {
    protected AuthorEntityBuilder() {}
  }
}
