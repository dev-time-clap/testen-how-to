package de.devtime.examples.library.businesslogic.object;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import de.devtime.examples.library.api.contract.publisher.PublisherRegistrationDto;
import de.devtime.examples.library.persistence.entity.PublisherEntity;
import de.devtime.examples.library.persistence.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PublisherFactory {

  private final ApplicationContext appContext;
  private final PublisherRepository publisherRepo;

  public Publisher createNew(final PublisherRegistrationDto registrationDto) {
    Publisher book = this.appContext.getBean(Publisher.class);
    PublisherEntity entityToRegister = PublisherEntity.builder()
        .withName(registrationDto.getName())
        .build()
        .generateId();
    book.init(entityToRegister);
    return book;
  }
}
