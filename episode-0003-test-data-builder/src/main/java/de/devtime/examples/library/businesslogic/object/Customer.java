package de.devtime.examples.library.businesslogic.object;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.devtime.examples.library.persistence.entity.CustomerEntity;
import de.devtime.examples.library.persistence.repository.BookRepository;
import de.devtime.examples.library.persistence.repository.CustomerRepository;
import de.devtime.examples.library.persistence.util.TransactionHelper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Customer extends AbstractBusinessObject<Customer> {

  private final TransactionHelper txHelper;
  private final CustomerRepository customerRepo;
  private final BookRepository bookRepo;
  private CustomerEntity entity;

  Customer init(final CustomerEntity entity) {
    this.entity = entity;
    return self();
  }

  public Customer loanBook(final Book bookToLoan) {
    // tbd
    return this;
  }

  public Customer notifyThatBookIsNotAvailableAnymore(final Book bookToLoanOut) {
    // tbd
    return this;
  }
}
