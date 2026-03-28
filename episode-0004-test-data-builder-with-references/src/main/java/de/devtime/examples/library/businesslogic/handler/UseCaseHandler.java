package de.devtime.examples.library.businesslogic.handler;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.helpers.MessageFormatter;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.devtime.examples.library.api.contract.book.BookLoanDto;
import de.devtime.examples.library.businesslogic.object.AuthorFactory;
import de.devtime.examples.library.businesslogic.object.Book;
import de.devtime.examples.library.businesslogic.object.BookFactory;
import de.devtime.examples.library.businesslogic.object.Customer;
import de.devtime.examples.library.businesslogic.object.CustomerFactory;
import de.devtime.examples.library.businesslogic.object.PublisherFactory;
import de.devtime.examples.library.event.command.LoanBookCommandEvent;
import de.devtime.examples.library.event.command.RegisterBookCommandEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class UseCaseHandler {

  private final AuthorFactory authorFactory;
  private final PublisherFactory publisherFactory;
  private final BookFactory bookFactory;
  private final CustomerFactory customerFactory;

  private List<Customer> interestedCustomers;

  @EventListener
  @Transactional
  void onRegisterBookCommandEvent(final RegisterBookCommandEvent event) {
    this.authorFactory
        .createNew(event.getAuthor())
        .register();
    this.publisherFactory
        .createNew(event.getPublisher())
        .register();
    this.bookFactory
        .createNew(event.getBook())
        .register();
  }

  @EventListener
  @Transactional
  void onLoanBookCommandEvent(final LoanBookCommandEvent event) {
    Objects.requireNonNull(event);
    Objects.requireNonNull(event.getBookLoan());

    BookLoanDto bookLoanDto = event.getBookLoan();
    Customer customer = verifyCustomerIsRegistered(bookLoanDto.getCustomerId());
    Book bookToLoanOut = verifyBookIsRegistered(bookLoanDto.getIsbn());
    bookToLoanOut.verifyBookIsAvailableToLoanOut();

    customer.loanBook(bookToLoanOut);
    bookToLoanOut.loanOut();

    for (Customer interestedCustomer : this.interestedCustomers) {
      interestedCustomer.notifyThatBookIsNotAvailableAnymore(bookToLoanOut);
    }
  }

  private Customer verifyCustomerIsRegistered(final UUID customerId) {
    Optional<Customer> optCustomer = this.customerFactory.findByCustomerId(customerId);
    if (optCustomer.isEmpty()) {
      throw new IllegalStateException(MessageFormatter
          .format("The customer with the ID {} is not yet registered in the system.", customerId.toString())
          .getMessage());
    }
    return optCustomer.get();
  }

  private Book verifyBookIsRegistered(final String isbn) {
    Optional<Book> optBook = this.bookFactory.findByIsbn(isbn);
    if (optBook.isEmpty()) {
      throw new IllegalStateException(MessageFormatter
          .format("The book is not yet registered in the system.", isbn)
          .getMessage());
    }
    return optBook.get();
  }
}
