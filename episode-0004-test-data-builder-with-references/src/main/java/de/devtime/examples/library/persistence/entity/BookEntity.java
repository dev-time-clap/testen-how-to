package de.devtime.examples.library.persistence.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "Book")
public class BookEntity extends AbstractEntity<BookEntity> {

  @Column(name = "ISBN", nullable = false)
  private String isbn;

  @Column(name = "TITLE", nullable = false)
  private String title;

  @Column(name = "IS_ON_LOAN")
  private boolean isOnLoan;

  //--------------------< Bi-directional links >--------------------

  // Primary link: A book has exactly one set of further details.
  @OneToOne
  @JoinColumn(name = "ADDITIONAL_DATA_ID")
  @Setter(AccessLevel.NONE)
  private AdditionalBookDataEntity additionalData;

  // Inverse link: A book can be offered by several publishers.
  @OneToMany(mappedBy = "book")
  @Builder.Default
  @ToString.Exclude
  @Setter(AccessLevel.NONE)
  private Set<BookPublisherEntity> bookPublishers = new HashSet<>();

  // Primary link: A book can be borrowed by exactly one customer.
  @ManyToOne
  @JoinColumn(name = "CUSTOMER_ID")
  @Setter(AccessLevel.NONE)
  private CustomerEntity customer;

  // Inverse link: A book can have multiple authors, but an author can also write multiple books.
  @ManyToMany(mappedBy = "books")
  @Builder.Default
  @ToString.Exclude
  @Setter(AccessLevel.NONE)
  private Set<AuthorEntity> authors = new HashSet<>();

  public void setAdditionalData(final AdditionalBookDataEntity additionalData) {
    // Avoid endless loops
    if (Objects.equals(this.additionalData, additionalData)) {
      log.debug("The additional data {} of the book {} already exist.", additionalData, this);
      return;
    }

    if (this.additionalData != null) {
      this.additionalData.setBook(null);
    }

    this.additionalData = additionalData;

    // Apply bi-directional link
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

    this.bookPublishers.add(bookPublisher);

    // Apply inverse link
    bookPublisher.setBook(this);
  }

  public void setCustomer(final CustomerEntity customer) {
    // Avoid endless loops
    if (Objects.equals(this.customer, customer)) {
      log.debug("The customer {} of the book {} already exist.", customer, this);
      return;
    }

    // Remember old customer to be able to remove it on the other side correctly
    CustomerEntity oldCustomer = this.customer;
    this.customer = customer;

    // Remove old inverse link
    if (oldCustomer != null) {
      oldCustomer.getLoanedBooks().remove(this);
    }

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

    this.authors.add(author);

    // Apply inverse link
    if (!author.getBooks().contains(this)) {
      author.getBooks().add(this);
    }
  }

  //--------------------< Builder-Pattern Support >--------------------

  public static class BookEntityBuilder<B> implements GenericBuilder<B> {
    protected BookEntityBuilder() {}
  }
}
