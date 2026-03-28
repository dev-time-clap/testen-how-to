package de.devtime.examples.library.persistence.entity;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "BOOK_PUBLISHER")
public class BookPublisherEntity extends AbstractEntity<BookPublisherEntity> {

  //--------------------< Bi-directional links >--------------------

  @ManyToOne
  @JoinColumn(name = "PUBLISHER_ID", nullable = false)
  @Setter(AccessLevel.NONE)
  private PublisherEntity publisher;

  @ManyToOne
  @JoinColumn(name = "BOOK_ID", nullable = false)
  @Setter(AccessLevel.NONE)
  private BookEntity book;

  public void setPublisher(final PublisherEntity publisher) {
    // Avoid endless loops
    if (Objects.equals(this.publisher, publisher)) {
      log.debug("The publisher {} of the book-publisher relation {} already exist.", publisher, this);
      return;
    }

    // Remember old publisher to be able to remove it on the other side correctly
    PublisherEntity oldPublisher = this.publisher;
    this.publisher = publisher;

    // Remove old inverse link
    if (oldPublisher != null) {
      oldPublisher.getBookPublishers().remove(this);
    }

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

    // Remember old book to be able to remove it on the other side correctly
    BookEntity oldBook = this.book;
    this.book = book;

    // Remove old inverse link
    if (oldBook != null) {
      oldBook.getBookPublishers().remove(this);
    }

    // Apply new inverse link
    if (book != null && !book.getBookPublishers().contains(this)) {
      book.getBookPublishers().add(this);
    }
  }

  //--------------------< Builder-Pattern Support >--------------------

  public static class BookPublisherEntityBuilder<B> implements GenericBuilder<B> {
    protected BookPublisherEntityBuilder() {}
  }
}
