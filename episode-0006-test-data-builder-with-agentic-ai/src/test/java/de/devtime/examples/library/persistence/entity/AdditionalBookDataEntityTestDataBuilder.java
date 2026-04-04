package de.devtime.examples.library.persistence.entity;

import java.util.UUID;
import java.util.function.Consumer;

import de.devtime.examples.library.persistence.entity.AdditionalBookDataEntity.AdditionalBookDataEntityBuilder;
import de.devtime.examples.library.test.builder.RecursionGuard;
import de.devtime.examples.library.test.builder.SaveContext;
import de.devtime.examples.library.test.builder.TestDataBuilder;
import de.devtime.examples.library.test.builder.TestDataBuilderWithSaveSupport;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AdditionalBookDataEntityTestDataBuilder<B extends TestDataBuilder<AdditionalBookDataEntity>>
    extends AdditionalBookDataEntityBuilder<B>
    implements TestDataBuilderWithSaveSupport<AdditionalBookDataEntity> {

  // --------------------< Add referenced builder here >--------------------

  private BookEntityTestDataProvider bookTestDataBuilder;

  public B withBook(final Consumer<BookEntityTestDataProvider> consumer) {
    RecursionGuard.guard(BookEntityTestDataProvider.class, () -> {
      log.info("consumer: {}", consumer);
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
  public String getUniqueTestDataSetKey(final AdditionalBookDataEntity entity) {
    return entity.getSummary();
  }

  @Override
  public AdditionalBookDataEntity buildInternally(
      final boolean withReferences,
      final boolean save,
      final SaveContext context) {
    AdditionalBookDataEntity entity = build().generateId();
    if (this.useExternalId) {
      entity.setId(this.id);
    }
    entity.setVersion(this.version);

    // Build referenced objects
    if (withReferences) {
      if (entity.getBook() == null) {
        entity.setBook(buildBook(entity, withReferences, save, context));
      }
    }

    // Save the entity
    if (save) {
      entity = save(entity, context);
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