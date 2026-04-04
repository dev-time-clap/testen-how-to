package de.devtime.examples.library.persistence.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "BOOK")
public class BookEntity extends AbstractEntity<BookEntity> {

  @Column(name = "ISBN", nullable = false)
  private String isbn;

  @Column(name = "TITLE", nullable = false)
  private String title;

  @Column(name = "IS_ON_LOAN")
  private boolean isOnLoan;

  // Primary link: A book has exactly one set of further details.
  @OneToOne
  @JoinColumn(name = "ADDITIONAL_DATA_ID")
  @Setter(AccessLevel.NONE)
  private AdditionalBookDataEntity additionalData;

  // Inverse link: A book can be offered by several publishers.
  @OneToMany(mappedBy = "book")
  @ToString.Exclude
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private Set<BookPublisherEntity> bookPublishers = new HashSet<>();

  // Primary link: A book can be borrowed by exactly one customer.
  @ManyToOne
  @JoinColumn(name = "CUSTOMER_ID")
  @Setter(AccessLevel.NONE)
  private CustomerEntity customer;

  // Inverse link: A book can have multiple authors, but an author can also write multiple books.
  @ManyToMany(mappedBy = "books")
  @ToString.Exclude
  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private Set<AuthorEntity> authors = new HashSet<>();

  public Set<BookPublisherEntity> getBookPublishers() {
    return Collections.unmodifiableSet(this.bookPublishers);
  }

  public Set<AuthorEntity> getAuthors() {
    return Collections.unmodifiableSet(this.authors);
  }

  //--------------------< Handle bi-directional relationships >--------------------

  public void setAdditionalData(final AdditionalBookDataEntity additionalData) {
    // Avoid endless loops
    if (Objects.equals(this.additionalData, additionalData)) {
      log.debug("The additional data {} of the book {} already exist.", additionalData, this);
      return;
    }

    // Remove old inverse link
    if (this.additionalData != null) {
      this.additionalData.setBook(null);
    }

    // Apply new primary link
    this.additionalData = additionalData;

    // Apply new inverse link
    if (additionalData != null) {
      additionalData.setBook(this);
    }
  }

  public void addBookPublisher(final BookPublisherEntity bookPublisher) {
    Objects.requireNonNull(bookPublisher);

    // Avoid endless loops
    if (this.bookPublishers.contains(bookPublisher)) {
      log.debug("The book-publisher relation {} of the book {} already exist.", bookPublisher, this);
      return;
    }

    // Apply new primary link
    this.bookPublishers.add(bookPublisher);

    // Apply new inverse link
    bookPublisher.setBook(this);
  }

  public void removeBookPublisher(final BookPublisherEntity bookPublisher) {
    Objects.requireNonNull(bookPublisher);

    // Avoid endless loop
    if (!this.bookPublishers.contains(bookPublisher)) {
      log.debug("The book-publisher relation {} of the book {} does not exist.", bookPublisher, this);
      return;
    }

    // Remove the primary link
    this.bookPublishers.remove(bookPublisher);

    // Remove the inverse link
    bookPublisher.setBook(null);
  }

  public void setCustomer(final CustomerEntity customer) {
    // Avoid endless loops
    if (Objects.equals(this.customer, customer)) {
      log.debug("The customer {} of the book {} already exist.", customer, this);
      return;
    }

    // Remove old inverse link
    if (this.customer != null) {
      this.customer.getLoanedBooks().remove(this);
    }

    // Apply new primary link
    this.customer = customer;

    // Apply new inverse link
    if (customer != null && !customer.getLoanedBooks().contains(this)) {
      customer.getLoanedBooks().add(this);
    }
  }

  public void addAuthor(final AuthorEntity author) {
    Objects.requireNonNull(author);

    // Avoid endless loops
    if (this.authors.contains(author)) {
      log.debug("The author {} of the book {} already exist.", author, this);
      return;
    }

    // Apply new primary link
    this.authors.add(author);

    // Apply inverse link
    if (!author.getBooks().contains(this)) {
      author.getBooks().add(this);
    }
  }

  public void removeAuthor(final AuthorEntity author) {
    Objects.requireNonNull(author);

    // Avoid endless loop
    if (!this.authors.contains(author)) {
      log.debug("The author {} of the book {} does not exist.", author, this);
      return;
    }

    // Remove the primary link
    this.authors.remove(author);

    // Remove the inverse link
    author.getBooks().remove(this);
  }

  //--------------------< Builder-Pattern Support >--------------------

  @Builder(setterPrefix = "with", toBuilder = true)
  private BookEntity(final UUID id,
      final int version,
      final String isbn,
      final String title,
      final boolean isOnLoan,
      final AdditionalBookDataEntity additionalData,
      final Set<BookPublisherEntity> bookPublishers,
      final CustomerEntity customer,
      final Set<AuthorEntity> authors) {
    super(id, version, false);
    this.isbn = isbn;
    this.title = title;
    this.isOnLoan = isOnLoan;
    this.additionalData = additionalData;
    this.bookPublishers = bookPublishers;
    this.customer = customer;
    this.authors = authors;
  }

  public static class BookEntityBuilder<B> implements GenericBuilder<B> {
    protected BookEntityBuilder() {}
  }
}
