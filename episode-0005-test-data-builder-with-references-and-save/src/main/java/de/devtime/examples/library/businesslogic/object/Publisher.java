package de.devtime.examples.library.businesslogic.object;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.devtime.examples.library.api.contract.publisher.PublisherDto;
import de.devtime.examples.library.event.domain.PublisherRegisteredDomainEvent;
import de.devtime.examples.library.persistence.entity.PublisherEntity;
import de.devtime.examples.library.persistence.repository.PublisherRepository;
import de.devtime.examples.library.persistence.util.TransactionHelper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Publisher extends AbstractBusinessObject<Publisher> {

  private final TransactionHelper txHelper;
  private final PublisherRepository publisherRepo;
  private PublisherEntity entity;

  Publisher init(final PublisherEntity entity) {
    this.entity = entity;
    return self();
  }

  @Transactional
  public Publisher register() {
    this.entity = this.publisherRepo.save(this.entity);
    this.txHelper.registerTaskAfterCommit(() -> firePublisherRegisteredDomainEvent(this.entity));
    return self();
  }

  private void firePublisherRegisteredDomainEvent(final PublisherEntity entity) {
    PublisherRegisteredDomainEvent.builder()
        .withPublisher(PublisherDto.builder()
            .withName(entity.getName())
            .withId(entity.getId())
            .build())
        .fire();
  }
}
