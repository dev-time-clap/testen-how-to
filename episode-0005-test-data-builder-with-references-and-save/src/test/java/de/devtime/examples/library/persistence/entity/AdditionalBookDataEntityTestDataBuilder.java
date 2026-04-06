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
  public String getUniqueTestDataSetKey(final AdditionalBookDataEntity entity) {
    return entity.getSummary();
  }

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

    // Build foreign referenced objects
    // None available

    // Save the entity
    if (save) {
      entity = context.saveWithDuplicateCheck(entity, this);
    }

    // Build inverse referenced objects
    if (withReferences) {
      // If the entity was not set via builder configuration directly, we try to build it via referenced builder
      if (entity.getBook() == null) {
        entity.setBook(buildBook(entity, withReferences, save, context));
      }
    }

    return entity;
  }

  private BookEntity buildBook(final AdditionalBookDataEntity entity, final boolean withReferences, final boolean save,
      final SaveContext context) {
    BookEntity referencedEntity = null;
    if (this.bookTestDataBuilder != null) {
      this.bookTestDataBuilder.withAdditionalData(entity);
      referencedEntity = this.bookTestDataBuilder.buildInternally(withReferences, save, context);
    }
    return referencedEntity;
  }
}