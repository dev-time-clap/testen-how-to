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

  @Column(name = "IS_ON_LOAN")
  private boolean isOnLoan;

  @Column(name = "TITLE", nullable = false)
  private String title;

  // Foreign link: A book has exactly one set of further details.
  @OneToOne
  @JoinColumn(name = "ADDITIONAL_DATA_ID")
  private AdditionalBookDataEntity additionalData;

  // Inverse link: A book can have multiple authors, but an author can also write multiple books.
  @ManyToMany(mappedBy = "author")
  @ToString.Exclude
  @Setter(AccessLevel.PACKAGE)
  private Set<BookAuthorEntity> bookAuthors = new HashSet<>();

  // Inverse link: A book can be offered by several publishers.
  @OneToMany(mappedBy = "book")
  @ToString.Exclude
  @Setter(AccessLevel.PACKAGE)
  private Set<BookPublisherEntity> bookPublishers = new HashSet<>();

  // Foreign link: A book can be borrowed by exactly one customer.
  @ManyToOne
  @JoinColumn(name = "CUSTOMER_ID")
  private CustomerEntity customer;

  //--------------------< Handle access and bi-directional relationships >--------------------

  public Set<BookPublisherEntity> getBookPublishers() {
    return Collections.unmodifiableSet(this.bookPublishers);
  }

  public Set<BookAuthorEntity> getBookAuthors() {
    return Collections.unmodifiableSet(this.bookAuthors);
  }

  public void setAdditionalData(final AdditionalBookDataEntity additionalData) {
    // Avoid endless loops
    if (Objects.equals(this.additionalData, additionalData)) {
      log.debug("The additional data {} of the book {} already exist.", additionalData, this);
      return;
    }

    // Apply new foreign link
    AdditionalBookDataEntity oldAdditionalBookDataEntity = this.additionalData;
    this.additionalData = additionalData;

    // Remove old inverse link
    if (oldAdditionalBookDataEntity != null) {
      oldAdditionalBookDataEntity.setBook(null);
    }

    // Apply new inverse link
    if (additionalData != null) {
      additionalData.setBook(this);
    }
  }

  public void addBookAuthor(final BookAuthorEntity bookAuthor) {
    Objects.requireNonNull(bookAuthor);

    // Avoid endless loops
    if (this.bookAuthors.contains(bookAuthor)) {
      log.debug("The book-author relation {} of the book {} already exist.", bookAuthor, this);
      return;
    }

    // Apply new foreign link
    this.bookAuthors.add(bookAuthor);

    // Apply inverse link
    bookAuthor.setBook(this);
  }

  public void removeBookAuthor(final BookAuthorEntity bookAuthor) {
    Objects.requireNonNull(bookAuthor);

    // Avoid endless loop
    if (!this.bookAuthors.contains(bookAuthor)) {
      log.debug("The book-author relation {} of the book {} does not exist.", bookAuthor, this);
      return;
    }

    // Remove the foreign link
    this.bookAuthors.remove(bookAuthor);

    // Remove the inverse link
    bookAuthor.setBook(null);
  }

  public void addBookPublisher(final BookPublisherEntity bookPublisher) {
    Objects.requireNonNull(bookPublisher);

    // Avoid endless loops
    if (this.bookPublishers.contains(bookPublisher)) {
      log.debug("The book-publisher relation {} of the book {} already exist.", bookPublisher, this);
      return;
    }

    // Apply new foreign link
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

    // Remove the foreign link
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
      this.customer.removeLoanedBook(this);
    }

    // Apply new foreign link
    this.customer = customer;

    // Apply new inverse link
    if (customer != null && !customer.getLoanedBooks().contains(this)) {
      customer.addLoanedBook(this);
    }
  }

  //--------------------< Builder-Pattern Support >--------------------

  @Builder(setterPrefix = "with", toBuilder = true)
  private BookEntity(
      final UUID id,
      final int version,
      final String isbn,
      final boolean isOnLoan,
      final String title,
      final Set<BookAuthorEntity> bookAuthors,
      final AdditionalBookDataEntity additionalData,
      final Set<BookPublisherEntity> bookPublishers,
      final CustomerEntity customer) {
    super(id, version, false);
    // Simple fields
    this.isbn = isbn;
    this.title = title;
    this.isOnLoan = isOnLoan;

    // Referenced entities
    this.additionalData = additionalData;
    this.bookAuthors = bookAuthors == null ? new HashSet<>() : bookAuthors;
    this.bookPublishers = bookPublishers == null ? new HashSet<>() : bookPublishers;
    this.customer = customer;
  }

  public static class BookEntityBuilder<B> implements GenericBuilder<B> {
    protected BookEntityBuilder() {}
  }
}
