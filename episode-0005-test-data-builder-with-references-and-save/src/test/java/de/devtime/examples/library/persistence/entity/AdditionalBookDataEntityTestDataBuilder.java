package de.devtime.examples.library.persistence.entity;

import java.util.function.Consumer;

import de.devtime.examples.library.persistence.entity.AdditionalBookDataEntity.AdditionalBookDataEntityBuilder;
import de.devtime.examples.library.test.builder.RecursionGuard;
import de.devtime.examples.library.test.builder.SaveContext;
import de.devtime.examples.library.test.builder.TestDataBuilder;
import de.devtime.examples.library.test.builder.TestDataBuilderWithSaveSupport;

public class AdditionalBookDataEntityTestDataBuilder<B extends TestDataBuilder<AdditionalBookDataEntity>>
    extends AdditionalBookDataEntityBuilder<B>
    implements TestDataBuilderWithSaveSupport<AdditionalBookDataEntity> {

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

  // --------------------< Internal builder logic >--------------------

  @Override
  public AdditionalBookDataEntity buildInternally(
      final boolean withReferences,
      final boolean save,
      final SaveContext context) {
    AdditionalBookDataEntity entity = build();
    // If the ID was not set via builder configuration, we have to generate a new ID
    if (entity.getId() == null) {
      entity.generateId();
    }

    // Build referenced objects
    if (withReferences) {
      entity.setBook(buildBook(withReferences));
    }

    // Save the entity
    if (save) {
      context.save(entity);
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