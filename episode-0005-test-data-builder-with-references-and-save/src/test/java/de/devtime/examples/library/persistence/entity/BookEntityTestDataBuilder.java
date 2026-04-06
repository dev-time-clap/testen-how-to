package de.devtime.examples.library.persistence.entity;

import java.util.function.Consumer;

import de.devtime.examples.library.persistence.entity.BookEntity.BookEntityBuilder;
import de.devtime.examples.library.test.builder.RecursionGuard;
import de.devtime.examples.library.test.builder.SaveContext;
import de.devtime.examples.library.test.builder.TestDataBuilder;
import de.devtime.examples.library.test.builder.TestDataBuilderWithSaveSupport;

public class BookEntityTestDataBuilder<B extends TestDataBuilder<BookEntity>>
    extends BookEntityBuilder<B>
    implements TestDataBuilderWithSaveSupport<BookEntity> {

  // --------------------< Add referenced builder here >--------------------

  private AdditionalBookDataEntityTestDataProvider additionalDataTestDataProvider;
  private CustomerEntityTestDataProvider customerTestDataProvider;

  public B withAdditionalData(final Consumer<AdditionalBookDataEntityTestDataProvider> consumer) {
    RecursionGuard.guard(AdditionalBookDataEntityTestDataProvider.class, () -> {
      this.additionalDataTestDataProvider = this.additionalDataTestDataProvider == null
          ? AdditionalBookDataEntityTestDataProvider.create()
          : this.additionalDataTestDataProvider;
      consumer.accept(this.additionalDataTestDataProvider);
    });
    return and();
  }

  public B withAdditionalData(final AdditionalBookDataEntityTestDataProvider provider) {
    this.additionalDataTestDataProvider = provider;
    return and();
  }

  public B withCustomer(final Consumer<CustomerEntityTestDataProvider> consumer) {
    RecursionGuard.guard(AdditionalBookDataEntityTestDataProvider.class, () -> {
      this.customerTestDataProvider = this.customerTestDataProvider == null
          ? CustomerEntityTestDataProvider.create()
          : this.customerTestDataProvider;
      consumer.accept(this.customerTestDataProvider);
    });
    return and();
  }

  public B withCustomer(final CustomerEntityTestDataProvider provider) {
    this.customerTestDataProvider = provider;
    return and();
  }

  // --------------------< Internal builder logic >--------------------

  @Override
  public String getUniqueTestDataSetKey(final BookEntity entity) {
    return entity.getIsbn();
  }

  @Override
  public BookEntity buildInternally(final boolean withReferences, final boolean save, final SaveContext context) {
    BookEntity entity = build();
    // If the ID was not set via builder configuration, we have to generate a new ID
    if (entity.getId() == null) {
      entity.generateId();
    }

    // Build foreign referenced objects
    if (withReferences) {
      // If the entity was not set via builder configuration directly, we try to build it via referenced builder
      if (entity.getAdditionalData() == null) {
        entity.setAdditionalData(buildAdditionalData(entity, withReferences, save, context));
      }
      // If the entity was not set via builder configuration directly, we try to build it via referenced builder
      if (entity.getCustomer() == null) {
        entity.setCustomer(buildCustomer(withReferences, save, context));
      }
    }

    // Save the entity
    if (save) {
      entity = context.saveWithDuplicateCheck(entity, this);
    }

    // Build inverse referenced objects
    // None available

    return entity;
  }

  private AdditionalBookDataEntity buildAdditionalData(final BookEntity entity, final boolean withReferences,
      final boolean save,
      final SaveContext context) {
    AdditionalBookDataEntity referencedEntity = null;
    if (this.additionalDataTestDataProvider != null) {
      this.additionalDataTestDataProvider.withBook(entity);
      referencedEntity = this.additionalDataTestDataProvider.buildInternally(withReferences, save, context);
    }
    return referencedEntity;
  }

  private CustomerEntity buildCustomer(final boolean withReferences, final boolean save,
      final SaveContext context) {
    CustomerEntity referencedEntity = null;
    if (this.customerTestDataProvider != null) {
      referencedEntity = this.customerTestDataProvider.buildInternally(withReferences,
          save, context);
    }
    return referencedEntity;
  }
}