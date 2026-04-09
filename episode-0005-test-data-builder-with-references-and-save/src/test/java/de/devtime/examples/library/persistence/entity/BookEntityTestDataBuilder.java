package de.devtime.examples.library.persistence.entity;

import java.util.function.Consumer;

import de.devtime.examples.library.persistence.entity.BookEntity.BookEntityBuilder;
import de.devtime.examples.library.test.builder.RecursionGuard;
import de.devtime.examples.library.test.builder.TestDataBuilder;

public class BookEntityTestDataBuilder<B extends TestDataBuilder<BookEntity>>
    extends BookEntityBuilder<B>
    implements TestDataBuilder<BookEntity> {

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
  public BookEntity buildInternally(final boolean withReferences) {
    BookEntity entity = build();
    // If the ID was not set via builder configuration, we have to generate a new ID
    if (entity.getId() == null) {
      entity.generateId();
    }

    // Build referenced objects
    if (withReferences) {
      entity.setAdditionalData(buildAdditionalData(withReferences));
      entity.setCustomer(buildCustomer(withReferences));
    }

    return entity;
  }

  private AdditionalBookDataEntity buildAdditionalData(final boolean withReferences) {
    AdditionalBookDataEntity referencedEntity = null;
    if (this.additionalDataTestDataProvider != null) {
      referencedEntity = this.additionalDataTestDataProvider.buildInternally(withReferences);
    }
    return referencedEntity;
  }

  private CustomerEntity buildCustomer(final boolean withReferences) {
    CustomerEntity referencedEntity = null;
    if (this.customerTestDataProvider != null) {
      referencedEntity = this.customerTestDataProvider.buildInternally(withReferences);
    }
    return referencedEntity;
  }

}