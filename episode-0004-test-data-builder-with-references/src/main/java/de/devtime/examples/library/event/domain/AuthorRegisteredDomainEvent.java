package de.devtime.examples.library.event.domain;

import de.devtime.examples.library.api.contract.author.AuthorDto;
import de.devtime.examples.library.context.AbstractManualAutowiredBean;
import de.devtime.examples.library.event.AbstractEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthorRegisteredDomainEvent extends AbstractEvent {

  @Getter
  private AuthorDto author;

  @Builder(setterPrefix = "with")
  public AuthorRegisteredDomainEvent(final AuthorDto author) {
    super();
    this.author = author;
  }

  public static class AuthorRegisteredDomainEventBuilder
      extends AbstractManualAutowiredBean<AuthorRegisteredDomainEventBuilder> {

    public void fire() {
      AuthorRegisteredDomainEvent eventToFire = build();
      log.info("Fire event {} with the following data: {}", eventToFire.getClass().getSimpleName(),
          eventToFire.getAuthor());
      this.appContext.publishEvent(eventToFire);
    }
  }
}
