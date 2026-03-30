package de.devtime.examples.library.businesslogic.object;

import org.slf4j.helpers.MessageFormatter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.devtime.examples.library.api.contract.book.BookDto;
import de.devtime.examples.library.event.domain.BookRegisteredDomainEvent;
import de.devtime.examples.library.persistence.entity.BookEntity;
import de.devtime.examples.library.persistence.repository.BookRepository;
import de.devtime.examples.library.persistence.util.TransactionHelper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Book extends AbstractBusinessObject<Book> {

  private final TransactionHelper txHelper;
  private final BookRepository bookRepo;

  @Getter(value = AccessLevel.PACKAGE)
  private BookEntity entity;

  Book init(final BookEntity entity) {
    this.entity = entity;
    return self();
  }

  public boolean isOnLoan() {
    return this.entity.isOnLoan();
  }

  public Book register() {
    this.entity = this.bookRepo.save(this.entity);
    this.txHelper.registerTaskAfterCommit(() -> fireBookRegisteredDomainEvent(this.entity));
    return self();
  }

  public void loanOut() {
    this.entity.setOnLoan(true);
    this.entity = this.bookRepo.save(this.entity);
    this.txHelper.registerTaskAfterCommit(() -> fireBookRegisteredDomainEvent(this.entity));
  }

  public void verifyBookIsAvailableToLoanOut() {
    if (this.entity.isOnLoan()) {
      throw new IllegalStateException(MessageFormatter
          .format("The book {} is already loaned out.", this.entity.getIsbn())
          .getMessage());
    }
  }

  private void fireBookRegisteredDomainEvent(final BookEntity entity) {
    BookRegisteredDomainEvent.builder()
        .withBook(BookDto.builder()
            .withIsbn(entity.getIsbn())
            .withTitle(entity.getTitle())
            .withId(entity.getId())
            .build())
        .fire();
  }

  private void fireBookLentDomainEvent(final BookEntity entity) {
    // tbd
  }

}
