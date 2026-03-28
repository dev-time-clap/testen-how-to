package de.devtime.examples.library.businesslogic.object;

import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import de.devtime.examples.library.api.contract.book.BookRegistrationDto;
import de.devtime.examples.library.persistence.entity.BookEntity;
import de.devtime.examples.library.persistence.repository.BookRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class BookFactory {

  private final ApplicationContext appContext;
  private final BookRepository bookRepo;

  public Book createNew(final BookRegistrationDto registrationDto) {
    Book book = this.appContext.getBean(Book.class);
    // TODO Das Mappen von Dto auf Entity sollte in einer eigenen Mapper-Komponente stattfinden
    BookEntity entityToRegister = BookEntity.builder()
        .withIsbn(registrationDto.getIsbn())
        .withTitle(registrationDto.getTitle())
        .withIsOnLoan(false)
        .build()
        .generateId();
    book.init(entityToRegister);
    return book;
  }

  public Optional<Book> findByIsbn(final String isbn) {
    return this.bookRepo
        .findByIsbn(isbn)
        .map(entity -> Optional.of(this.appContext.getBean(Book.class).init(entity)))
        .orElse(Optional.empty());
  }
}
