package de.devtime.examples.library.persistence.entity;

import java.util.function.Consumer;

import de.devtime.examples.library.persistence.entity.AdditionalBookDataEntity.AdditionalBookDataEntityBuilder;
import de.devtime.examples.library.test.builder.RecursionGuard;
import de.devtime.examples.library.test.builder.SaveContext;
import de.devtime.examples.library.test.builder.TestDataBuilder;
import de.devtime.examples.library.test.builder.TestDataBuilderWithSaveSupport;

public abstract class AdditionalBookDataEntityTestDataBuilder<B extends TestDataBuilder<AdditionalBookDataEntity>>
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
    // None available

    // Save the entity
    if (save) {
      entity = context.saveWithDuplicateCheck(entity, this);
    }

    // Build referenced objects
    if (withReferences) {
      entity.setBook(buildBook(entity, withReferences, save, context));
    }

    return entity;
  }

  private BookEntity buildBook(
      final AdditionalBookDataEntity entity,
      final boolean withReferences,
      final boolean save,
      final SaveContext context) {
    if (entity.getBook() != null) {
      return entity.getBook();
    }
    BookEntity referencedEntity = null;
    if (this.bookTestDataBuilder != null) {
      this.bookTestDataBuilder.withAdditionalData(entity);
      referencedEntity = this.bookTestDataBuilder.buildInternally(withReferences, save, context);
    }
    return referencedEntity;
  }
}