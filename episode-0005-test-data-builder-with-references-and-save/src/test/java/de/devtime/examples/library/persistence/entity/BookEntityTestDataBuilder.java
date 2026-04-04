package de.devtime.examples.library.persistence.entity;

import java.util.UUID;
import java.util.function.Consumer;

import de.devtime.examples.library.persistence.entity.BookEntity.BookEntityBuilder;
import de.devtime.examples.library.persistence.repository.BookRepository;
import de.devtime.examples.library.test.builder.RecursionGuard;
import de.devtime.examples.library.test.builder.SaveContext;
import de.devtime.examples.library.test.builder.TestDataBuilder;
import de.devtime.examples.library.test.builder.TestDataBuilderWithSaveSupport;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookEntityTestDataBuilder<B extends TestDataBuilder<BookEntity>>
    extends BookEntityBuilder<B>
    implements TestDataBuilderWithSaveSupport<BookEntity, BookRepository> {

  // --------------------< Add referenced builder here >--------------------

  private AdditionalBookDataEntityTestDataProvider additionalDataTestDataProvider;
  private CustomerEntityTestDataProvider customerTestDataProvider;

  public B withAdditionalData(final Consumer<AdditionalBookDataEntityTestDataProvider> consumer) {
    RecursionGuard.guard(AdditionalBookDataEntityTestDataProvider.class, () -> {
      log.info("consumer: {}", consumer);
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
  public String getUniqueDataSetKey(final BookEntity entity) {
    return entity.getIsbn();
  }

  @Override
  public BookEntity buildInternally(final boolean withReferences, final boolean save, final SaveContext context) {
    BookEntity entity = build().generateId();
    if (this.useExternalId) {
      entity.setId(this.id);
    }
    entity.setVersion(this.version);

    // Build parent referenced objects
    if (withReferences) {
      entity.setCustomer(buildCustomer(withReferences, save, context));
    }

    // Save the entity
    if (save) {
      entity = save(entity, context);
    }

    // Build child referenced objects
    if (withReferences) {
      if (entity.getAdditionalData() == null) {
        entity.setAdditionalData(buildAdditionalData(entity, withReferences, save, context));
      }
    }

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