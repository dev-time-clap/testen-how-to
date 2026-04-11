package de.devtime.examples.library.persistence.entity;

import java.time.LocalDate;

public class AuthorEntityTestDataProvider extends AuthorEntityTestDataBuilder<AuthorEntityTestDataProvider> {

  public static AuthorEntityTestDataProvider create() {
    return new AuthorEntityTestDataProvider();
  }

  public AuthorEntityTestDataProvider authorMorrigan() {
    withFirstName(null);
    withLastName(null);
    withArtistName("Morrigan");
    withBirthday(LocalDate.of(2004, 12, 11));
    return and();
  }

  public AuthorEntityTestDataProvider authorStephenKing() {
    withFirstName("Stephen");
    withLastName("King");
    withArtistName("Stephen King");
    withBirthday(LocalDate.of(1947, 9, 21));
    return and();
  }

  public AuthorEntityTestDataProvider authorGeorgeOrwell() {
    withFirstName("Eric Arthur");
    withLastName("Blair");
    withArtistName("George Orwell");
    withBirthday(LocalDate.of(1903, 6, 25));
    return and();
  }

  public AuthorEntityTestDataProvider anonymousAuthor() {
    withArtistName("Anonymus");
    return and();
  }

  @Override
  public String getUniqueTestDataSetKey(final AuthorEntity entity) {
    return entity.getArtistName();
  }
}
