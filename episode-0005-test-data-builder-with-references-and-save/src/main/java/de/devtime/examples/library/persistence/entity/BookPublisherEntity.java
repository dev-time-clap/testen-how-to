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
@Table(name = "BOOK_PUBLISHER")
public class BookPublisherEntity extends AbstractEntity<BookPublisherEntity> {

  @ManyToOne
  @JoinColumn(name = "PUBLISHER_ID", nullable = false)
  @Setter(AccessLevel.NONE)
  private PublisherEntity publisher;

  @ManyToOne
  @JoinColumn(name = "BOOK_ID", nullable = false)
  @Setter(AccessLevel.NONE)
  private BookEntity book;

  //--------------------< Handle access and bi-directional relationships >--------------------

  public void setPublisher(final PublisherEntity publisher) {
    // Avoid endless loops
    if (Objects.equals(this.publisher, publisher)) {
      log.debug("The publisher {} of the book-publisher relation {} already exist.", publisher, this);
      return;
    }

    // Remove old inverse link
    if (this.publisher != null) {
      this.publisher.getBookPublishers().remove(this);
    }

    // Apply new foreign link
    this.publisher = publisher;

    // Apply new inverse link
    if (publisher != null && !publisher.getBookPublishers().contains(this)) {
      publisher.getBookPublishers().add(this);
    }
  }

  public void setBook(final BookEntity book) {
    // Avoid endless loops
    if (Objects.equals(this.book, book)) {
      log.debug("The book {} of the book-publisher relation {} already exist.", book, this);
      return;
    }

    // Remove old inverse link
    if (this.book != null) {
      this.book.getBookPublishers().remove(this);
    }

    // Apply new foreign link
    this.book = book;

    // Apply new inverse link
    if (book != null && !book.getBookPublishers().contains(this)) {
      book.getBookPublishers().add(this);
    }
  }

  //--------------------< Builder-Pattern Support >--------------------

  @Builder(setterPrefix = "with", toBuilder = true)
  private BookPublisherEntity(
      final UUID id,
      final int version,
      final BookEntity book,
      final PublisherEntity publisher) {
    // Simple fields
    super(id, version, false);

    // Referenced entities
    this.book = book;
    this.publisher = publisher;
  }

  public static class BookPublisherEntityBuilder<B> implements GenericBuilder<B> {
    protected BookPublisherEntityBuilder() {}
  }
}
