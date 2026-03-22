package de.devtime.examples.library.persistence.entity;

import java.util.UUID;

import de.devtime.examples.library.persistence.entity.BookEntity.BookEntityBuilder;
import de.devtime.examples.library.test.builder.TestDataBuilder;

public class BookEntityTestDataBuilder<B extends TestDataBuilder<BookEntity>>
    extends BookEntityBuilder<B>
    implements TestDataBuilder<BookEntity> {

  // --------------------< Add super fields here >--------------------

  private UUID id;
  private int version;
  private boolean useExternalId = false;

  public B withId(final UUID id) {
    this.id = id;
    this.useExternalId = true;
    return and();
  }

  public B withVersion(final int version) {
    this.version = version;
    return and();
  }

  // --------------------< Internal builder logic >--------------------

  @Override
  public BookEntity buildInternally() {
    BookEntity entity = build().generateId();
    if (this.useExternalId) {
      entity.setId(this.id);
    }
    entity.setVersion(this.version);
    return entity;
  }
}