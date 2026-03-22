package de.devtime.examples.library.event.domain;

import de.devtime.examples.library.api.contract.publisher.PublisherDto;
import de.devtime.examples.library.context.AbstractManualAutowiredBean;
import de.devtime.examples.library.event.AbstractEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PublisherRegisteredDomainEvent extends AbstractEvent {

  @Getter
  private PublisherDto publisher;

  @Builder(setterPrefix = "with")
  public PublisherRegisteredDomainEvent(final PublisherDto publisher) {
    super();
    this.publisher = publisher;
  }

  public static class PublisherRegisteredDomainEventBuilder
      extends AbstractManualAutowiredBean<PublisherRegisteredDomainEventBuilder> {

    public void fire() {
      PublisherRegisteredDomainEvent eventToFire = build();
      log.info("Fire event {} with the following data: {}", eventToFire.getClass().getSimpleName(),
          eventToFire.getPublisher());
      this.appContext.publishEvent(eventToFire);
    }
  }
}
