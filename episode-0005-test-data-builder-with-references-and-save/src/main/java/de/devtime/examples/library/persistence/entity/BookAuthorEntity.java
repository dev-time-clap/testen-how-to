package de.devtime.examples.library.persistence.entity;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "BOOK_AUTHOR")
public class BookAuthorEntity extends AbstractEntity<BookAuthorEntity> {

  @ManyToOne
  @JoinColumn(name = "AUTHOR_ID", nullable = false)
  @Setter(AccessLevel.NONE)
  private AuthorEntity author;

  @ManyToOne
  @JoinColumn(name = "BOOK_ID", nullable = false)
  @Setter(AccessLevel.NONE)
  private BookEntity book;

  //--------------------< Handle access and bi-directional relationships >--------------------

  public void setAuthor(final AuthorEntity author) {
    // Avoid endless loops
    if (Objects.equals(this.author, author)) {
      log.debug("The author {} of the book-authos relation {} already exist.", author, this);
      return;
    }

    // Remove old inverse link
    if (this.author != null) {
      this.author.getBookAuthors().remove(this);
    }

    // Apply new foreign link
    this.author = author;

    // Apply new inverse link
    if (author != null && !author.getBookAuthors().contains(this)) {
      author.getBookAuthors().add(this);
    }
  }

  public void setBook(final BookEntity book) {
    // Avoid endless loops
    if (Objects.equals(this.book, book)) {
      log.debug("The book {} of the book-author relation {} already exist.", book, this);
      return;
    }

    // Remove old inverse link
    if (this.book != null) {
      this.book.getBookAuthors().remove(this);
    }

    // Apply new foreign link
    this.book = book;

    // Apply new inverse link
    if (book != null && !book.getBookAuthors().contains(this)) {
      book.getBookAuthors().add(this);
    }
  }

  //--------------------< Builder-Pattern Support >--------------------

  @Builder(setterPrefix = "with", toBuilder = true)
  private BookAuthorEntity(
      final UUID id,
      final int version,
      final BookEntity book,
      final AuthorEntity author) {
    // Simple fields
    super(id, version, false);

    // Referenced entities
    this.book = book;
    this.author = author;
  }

  public static class BookAuthorEntityBuilder<B> implements GenericBuilder<B> {
    protected BookAuthorEntityBuilder() {}
  }
}
