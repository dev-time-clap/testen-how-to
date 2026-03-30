package de.devtime.examples.library.event.domain;

import de.devtime.examples.library.api.contract.book.BookDto;
import de.devtime.examples.library.context.AbstractManualAutowiredBean;
import de.devtime.examples.library.event.AbstractEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookRegisteredDomainEvent extends AbstractEvent {

  @Getter
  private BookDto book;

  @Builder(setterPrefix = "with")
  public BookRegisteredDomainEvent(final BookDto book) {
    super();
    this.book = book;
  }

  public static class BookRegisteredDomainEventBuilder
      extends AbstractManualAutowiredBean<BookRegisteredDomainEventBuilder> {

    public void fire() {
      BookRegisteredDomainEvent eventToFire = build();
      log.info("Fire event {} with the following data: {}", eventToFire.getClass().getSimpleName(),
          eventToFire.getBook());
      this.appContext.publishEvent(eventToFire);
    }
  }
}
