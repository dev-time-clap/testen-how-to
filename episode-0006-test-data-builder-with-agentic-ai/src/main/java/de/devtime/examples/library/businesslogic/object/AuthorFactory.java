package de.devtime.examples.library.businesslogic.object;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import de.devtime.examples.library.api.contract.author.AuthorRegistrationDto;
import de.devtime.examples.library.persistence.entity.AuthorEntity;
import de.devtime.examples.library.persistence.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class AuthorFactory {

  private final ApplicationContext appContext;
  private final AuthorRepository authorRepo;

  public Author createNew(final AuthorRegistrationDto registrationDto) {
    Author author = this.appContext.getBean(Author.class);
    AuthorEntity entityToRegister = AuthorEntity.builder()
        .withArtistName(registrationDto.getArtistName())
        .withFirstName(registrationDto.getFirstName())
        .withLastName(registrationDto.getLastName())
        .withBirthday(registrationDto.getBirthday())
        .build()
        .generateId();
    author.init(entityToRegister);
    return author;
  }
}
