package de.devtime.examples.library.event.command;

import de.devtime.examples.library.api.contract.author.AuthorRegistrationDto;
import de.devtime.examples.library.api.contract.book.BookRegistrationDto;
import de.devtime.examples.library.api.contract.publisher.PublisherRegistrationDto;
import de.devtime.examples.library.context.AbstractManualAutowiredBean;
import de.devtime.examples.library.event.AbstractEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegisterBookCommandEvent extends AbstractEvent {

  @Getter
  private BookRegistrationDto book;

  @Getter
  private AuthorRegistrationDto author;

  @Getter
  private PublisherRegistrationDto publisher;

  @Builder(setterPrefix = "with")
  public RegisterBookCommandEvent(
      final BookRegistrationDto book,
      final AuthorRegistrationDto author,
      final PublisherRegistrationDto publisher) {
    super();
    this.book = book;
    this.author = author;
    this.publisher = publisher;
  }

  public static class RegisterBookCommandEventBuilder
      extends AbstractManualAutowiredBean<RegisterBookCommandEventBuilder> {

    public void fire() {
      RegisterBookCommandEvent eventToFire = build();
      log.info("Fire event: {}", eventToFire.getClass().getSimpleName());
      this.appContext.publishEvent(eventToFire);
    }
  }
}
