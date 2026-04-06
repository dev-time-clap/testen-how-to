package de.devtime.examples.library.persistence.entity;

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
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@Getter
@Setter

@Entity
@Table(name = "Customer")
public class CustomerEntity extends AbstractEntity<CustomerEntity> {

  @Column(name = "FIRST_NAME", nullable = false)
  private String firstName;

  @Column(name = "LAST_NAME", nullable = false)
  private String lastName;

  @Column(name = "NUMBER", nullable = false)
  private String number;

  // Inverse link: A customer can borrow multiple books
  @OneToMany(mappedBy = "customer")
  @ToString.Exclude
  @Setter(AccessLevel.NONE)
  private Set<BookEntity> loanedBooks = new HashSet<>();

  //--------------------< Handle access and bi-directional relationships >--------------------

  public Set<BookEntity> getLoanedBooks() {
    return Collections.unmodifiableSet(this.loanedBooks);
  }

  public void addLoanedBook(final BookEntity book) {
    Objects.requireNonNull(book);

    // Avoid endless loops
    if (this.loanedBooks.contains(book)) {
      log.debug("The book {} is already loaned by customer {}.", book, this);
      return;
    }

    // Apply new foreign link
    this.loanedBooks.add(book);

    // Apply inverse link
    book.setCustomer(this);
  }

  public void removeLoanedBook(final BookEntity book) {
    Objects.requireNonNull(book);

    // Avoid endless loop
    if (!this.loanedBooks.contains(book)) {
      log.debug("The book {} of the customer {} does not exist.", book, this);
      return;
    }

    // Remove the foreign link
    this.loanedBooks.remove(book);

    // Remove the inverse link
    book.setCustomer(null);
  }

  //--------------------< Builder-Pattern Support >--------------------

  @Builder(setterPrefix = "with", toBuilder = true)
  private CustomerEntity(
      final UUID id,
      final int version,
      final String firstName,
      final String lastName,
      final String number,
      final Set<BookEntity> loanedBooks) {
    // Simple fields
    super(id, version, false);
    this.firstName = firstName;
    this.lastName = lastName;
    this.number = number;

    // Referenced entities
    this.loanedBooks = loanedBooks == null ? new HashSet<>() : loanedBooks;
  }

  public static class CustomerEntityBuilder<B> implements GenericBuilder<B> {
    protected CustomerEntityBuilder() {}
  }
}
