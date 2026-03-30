package de.devtime.examples.library.persistence.entity;

import java.util.UUID;
import java.util.function.Consumer;

import de.devtime.examples.library.persistence.entity.AdditionalBookDataEntity.AdditionalBookDataEntityBuilder;
<<<<<<< HEAD
=======
import de.devtime.examples.library.test.builder.RecursionGuard;
>>>>>>> 9b7c0383fbee4773ead58dd8e4bf3ca1360e0442
import de.devtime.examples.library.test.builder.TestDataBuilder;

public class AdditionalBookDataEntityTestDataBuilder<B extends TestDataBuilder<AdditionalBookDataEntity>>
    extends AdditionalBookDataEntityBuilder<B>
    implements TestDataBuilder<AdditionalBookDataEntity> {

  // --------------------< Add referenced builder here >--------------------

  private BookEntityTestDataProvider bookTestDataBuilder;

  public B withBook(final Consumer<BookEntityTestDataProvider> consumer) {
    RecursionGuard.guard(BookEntityTestDataProvider.class, () -> {
      this.bookTestDataBuilder = this.bookTestDataBuilder == null
          ? BookEntityTestDataProvider.create()
          : this.bookTestDataBuilder;
      consumer.accept(this.bookTestDataBuilder);
    });
    return and();
  }

  public B withBook(final BookEntityTestDataProvider builder) {
    this.bookTestDataBuilder = builder;
    return and();
  }

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
  public AdditionalBookDataEntity buildInternally(final boolean withReferences) {
    AdditionalBookDataEntity entity = build().generateId();
    if (this.useExternalId) {
      entity.setId(this.id);
    }
    entity.setVersion(this.version);

    // Build referenced objects
    if (withReferences) {
      entity.setBook(buildBook(withReferences));
    }
    return entity;
  }

  private BookEntity buildBook(final boolean withReferences) {
    BookEntity referencedEntity = null;
    if (this.bookTestDataBuilder != null) {
      referencedEntity = this.bookTestDataBuilder.buildInternally(withReferences);
    }
    return referencedEntity;
  }
}