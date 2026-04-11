package de.devtime.examples.library.persistence.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.devtime.examples.library.persistence.entity.CustomerEntity.CustomerEntityBuilder;
import de.devtime.examples.library.test.builder.SaveContext;
import de.devtime.examples.library.test.builder.TestDataBuilder;
import de.devtime.examples.library.test.builder.TestDataBuilderWithSaveSupport;

public abstract class CustomerEntityTestDataBuilder<B extends TestDataBuilder<CustomerEntity>>
    extends CustomerEntityBuilder<B>
    implements TestDataBuilderWithSaveSupport<CustomerEntity> {

  // --------------------< Add referenced builder here >--------------------

  private final List<BookEntityTestDataProvider> bookTestDataProviders = new ArrayList<BookEntityTestDataProvider>();

  public B withLoanedBook(final Consumer<BookEntityTestDataProvider> consumer) {
    BookEntityTestDataProvider builder = BookEntityTestDataProvider.create();
    consumer.accept(builder);
    this.bookTestDataProviders.add(builder);
    return and();
  }

  public B withLoanedBook(final BookEntityTestDataProvider bookTestDataBuilder) {
    this.bookTestDataProviders.add(bookTestDataBuilder);
    return and();
  }

  // --------------------< Internal builder logic >--------------------

  @Override
  public CustomerEntity buildInternally(
      final boolean withReferences,
      final boolean save,
      final SaveContext context) {
    CustomerEntity entity = build();
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
      buildLoanedBooks(entity, withReferences, save, context).forEach(entity::addLoanedBook);
    }
    return entity;
  }

  private Set<BookEntity> buildLoanedBooks(
      final CustomerEntity entity,
      final boolean withReferences,
      final boolean save,
      final SaveContext context) {
    if (entity.getLoanedBooks() != null && !entity.getLoanedBooks().isEmpty()) {
      return entity.getLoanedBooks();
    }
    return this.bookTestDataProviders.stream()
        .map(provider -> {
          provider.withCustomer(entity);
          return provider.buildInternally(withReferences, save, context);
        })
        .collect(Collectors.toSet());
  }
}
