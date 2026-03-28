package de.devtime.examples.library.persistence.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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
@Table(name = "Customer")
public class CustomerEntity extends AbstractEntity<CustomerEntity> {

  @Column(name = "FIRST_NAME", nullable = false)
  private String firstName;

  @Column(name = "LAST_NAME", nullable = false)
  private String lastName;

  @Column(name = "NUMBER", nullable = false)
  private String number;

  //--------------------< Bi-directional links >--------------------

  @OneToMany(mappedBy = "customer")
  @Builder.Default
  @ToString.Exclude
  @Setter(AccessLevel.NONE)
  private final Set<BookEntity> loanedBooks = new HashSet<>();

  public void addLoanedBook(final BookEntity book) {
    Objects.requireNonNull(book);

    // Avoid endless loops
    if (this.loanedBooks.contains(book)) {
      log.debug("The book {} is already loaned by customer {}.", book, this);
      return;
    }

    this.loanedBooks.add(book);

    // Apply inverse link
    book.setCustomer(this);
  }

  //--------------------< Builder-Pattern Support >--------------------
  public static class CustomerEntityBuilder<B> implements GenericBuilder<B> {
    protected CustomerEntityBuilder() {}
  }
}
