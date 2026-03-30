package de.devtime.examples.library.persistence.entity;

import java.util.UUID;
import java.util.function.Consumer;

import de.devtime.examples.library.persistence.entity.BookEntity.BookEntityBuilder;
import de.devtime.examples.library.test.builder.TestDataBuilder;

public class BookEntityTestDataBuilder<B extends TestDataBuilder<BookEntity>>
    extends BookEntityBuilder<B>
    implements TestDataBuilder<BookEntity> {

  // --------------------< Add referenced builder here >--------------------

  private AdditionalBookDataEntityTestDataProvider additionalDataTestDataProvider;
  private CustomerEntityTestDataProvider customerTestDataProvider;

  public B withAdditionalData(final Consumer<AdditionalBookDataEntityTestDataProvider> consumer) {
    this.additionalDataTestDataProvider = this.additionalDataTestDataProvider == null
        ? AdditionalBookDataEntityTestDataProvider.create()
        : this.additionalDataTestDataProvider;
    consumer.accept(this.additionalDataTestDataProvider);
    return and();
  }

  public B withAdditionalData(final AdditionalBookDataEntityTestDataProvider provider) {
    this.additionalDataTestDataProvider = provider;
    return and();
  }

  public B withCustomer(final Consumer<CustomerEntityTestDataProvider> consumer) {
    this.customerTestDataProvider = this.customerTestDataProvider == null
        ? CustomerEntityTestDataProvider.create()
        : this.customerTestDataProvider;
    consumer.accept(this.customerTestDataProvider);
    return and();
  }

  public B withCustomer(final CustomerEntityTestDataProvider provider) {
    this.customerTestDataProvider = provider;
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
  public BookEntity buildInternally(final boolean withReferences) {
    BookEntity entity = build().generateId();
    if (this.useExternalId) {
      entity.setId(this.id);
    }
    entity.setVersion(this.version);

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