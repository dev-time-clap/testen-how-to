package de.devtime.examples.library.businesslogic.object;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import de.devtime.examples.library.api.contract.author.AuthorDto;
import de.devtime.examples.library.event.domain.AuthorRegisteredDomainEvent;
import de.devtime.examples.library.persistence.entity.AuthorEntity;
import de.devtime.examples.library.persistence.repository.AuthorRepository;
import de.devtime.examples.library.persistence.util.TransactionHelper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Author extends AbstractBusinessObject<Author> {

  private final TransactionHelper txHelper;
  private final AuthorRepository authorRepo;
  private AuthorEntity entity;

  Author init(final AuthorEntity entity) {
    this.entity = entity;
    return self();
  }

  @Transactional
  public Author register() {
    this.entity = this.authorRepo.save(this.entity);
    this.txHelper.registerTaskAfterCommit(() -> fireAuthorRegisteredDomainEvent(this.entity));
    return self();
  }

  void fireAuthorRegisteredDomainEvent(final AuthorEntity entity) {
    AuthorRegisteredDomainEvent.builder()
        .withAuthor(AuthorDto.builder()
            .withFirstName(entity.getFirstName())
            .withLastName(entity.getLastName())
            .withArtistName(entity.getArtistName())
            .withBirthday(entity.getBirthday())
            .withId(entity.getId())
            .build())
        .fire();
  }
}
