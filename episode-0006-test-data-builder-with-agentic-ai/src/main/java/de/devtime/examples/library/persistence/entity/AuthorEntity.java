package de.devtime.examples.library.persistence.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(setterPrefix = "with")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@Getter
@Setter

@Entity
@Table(name = "Author")
public class AuthorEntity extends AbstractEntity<AuthorEntity> {

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "ARTIST_NAME", nullable = false)
  private String artistName;

  @Column(name = "BIRTHDAY")
  private LocalDate birthday;

  //--------------------< Bi-directional links >--------------------

  @ManyToMany
  @JoinTable(name = "AUTHOR_BOOK", joinColumns = @JoinColumn(name = "AUTHOR_ID"), inverseJoinColumns = @JoinColumn(name = "BOOK_ID"))
  @Builder.Default
  @ToString.Exclude
  @Setter(AccessLevel.NONE)
  private final Set<BookEntity> books = new HashSet<>();

  public void addBook(final BookEntity book) {
    Objects.requireNonNull(book);

    // Avoid endless loops
    if (this.books.contains(book)) {
      log.debug("The book {} of the author {} already exist.", book, this);
      return;
    }

    this.books.add(book);

    // Apply inverse link
    if (!book.getAuthors().contains(this)) {
      book.getAuthors().add(this);
    }
  }

  //--------------------< Builder-Pattern Support >--------------------

  public static class AuthorEntityBuilder<B> implements GenericBuilder<B> {
    protected AuthorEntityBuilder() {}
  }
}
