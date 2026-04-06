package de.devtime.examples.library.persistence.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

  //--------------------< Bi-directional links >--------------------

  @ManyToMany
  @JoinTable(name = "AUTHOR_BOOK", joinColumns = @JoinColumn(name = "AUTHOR_ID"), inverseJoinColumns = @JoinColumn(name = "BOOK_ID"))
  @ToString.Exclude
  @Setter(AccessLevel.NONE)
  private Set<BookEntity> books = new HashSet<>();

  //--------------------< Handle bi-directional relationships >--------------------

  public void addBook(final BookEntity book) {
    Objects.requireNonNull(book);

    // Avoid endless loops
    if (this.books.contains(book)) {
      log.debug("The book {} of the author {} already exist.", book, this);
      return;
    }

    // Apply new foreign link
    this.books.add(book);

    // Apply inverse link
    if (!book.getAuthors().contains(this)) {
      book.getAuthors().add(this);
    }
  }

  public void removeBook(final BookEntity book) {
    Objects.requireNonNull(book);

    // Avoid endless loops
    if (!this.books.contains(book)) {
      log.debug("The book {} is not associated with the author {}", book, this);
      return;
    }

    // Remove foreign link
    this.books.remove(book);

    // Remove inverse link
    book.getAuthors().remove(this);
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
      final Set<BookEntity> books) {
    // Simple fields
    super(id, version, false);
    this.artistName = artistName;
    this.birthday = birthday;
    this.firstName = firstName;
    this.lastName = lastName;

    // Referenced entities
    this.books = books == null ? new HashSet<>() : books;
  }

  public static class AuthorEntityBuilder<B> implements GenericBuilder<B> {
    protected AuthorEntityBuilder() {}
  }
}
