package de.devtime.examples.library.persistence.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import de.devtime.examples.library.persistence.entity.CustomerEntity.CustomerEntityBuilder;
import de.devtime.examples.library.test.builder.TestDataBuilder;

public class CustomerEntityTestDataBuilder<B extends TestDataBuilder<CustomerEntity>>
    extends CustomerEntityBuilder<B>
    implements TestDataBuilder<CustomerEntity> {

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
  public CustomerEntity buildInternally(final boolean withReferences) {
    CustomerEntity entity = build();
    // If the ID was not set via builder configuration, we have to generate a new ID
    if (entity.getId() == null) {
      entity.generateId();
    }

    // Build referenced objects
    if (withReferences) {
      buildLoanedBooks(withReferences).forEach(entity::addLoanedBook);
    }
    return entity;
  }

  private List<BookEntity> buildLoanedBooks(final boolean withReferences) {
    return this.bookTestDataProviders.stream()
        .map(provider -> provider.buildInternally(withReferences))
        .toList();
  }
}
