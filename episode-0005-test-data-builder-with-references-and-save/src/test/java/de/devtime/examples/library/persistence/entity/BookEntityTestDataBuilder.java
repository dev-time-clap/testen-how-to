package de.devtime.examples.library.persistence.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.devtime.examples.library.persistence.entity.BookEntity.BookEntityBuilder;
import de.devtime.examples.library.test.builder.RecursionGuard;
import de.devtime.examples.library.test.builder.SaveContext;
import de.devtime.examples.library.test.builder.TestDataBuilder;
import de.devtime.examples.library.test.builder.TestDataBuilderWithSaveSupport;

public abstract class BookEntityTestDataBuilder<B extends TestDataBuilder<BookEntity>>
    extends BookEntityBuilder<B>
    implements TestDataBuilderWithSaveSupport<BookEntity> {

  // --------------------< Add referenced builder here >--------------------

  private AdditionalBookDataEntityTestDataProvider additionalDataTestDataProvider;
  private CustomerEntityTestDataProvider customerTestDataProvider;
  private final List<AuthorEntityTestDataProvider> authorTestDataProviders = new ArrayList<>();

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

  public B withAuthor(final Consumer<AuthorEntityTestDataProvider> consumer) {
    AuthorEntityTestDataProvider builder = AuthorEntityTestDataProvider.create();
    consumer.accept(builder);
    this.authorTestDataProviders.add(builder);
    return and();
  }

  public B withAuthor(final AuthorEntityTestDataProvider provider) {
    this.authorTestDataProviders.add(provider);
    return and();
  }

  // --------------------< Internal builder logic >--------------------

  @Override
  public BookEntity buildInternally(
      final boolean withReferences,
      final boolean save,
      final SaveContext context) {
    BookEntity entity = build();
    // If the ID was not set via builder configuration, we have to generate a new ID
    if (entity.getId() == null) {
      entity.generateId();
    }

    // Build referenced objects
    if (withReferences) {
      entity.setAdditionalData(buildAdditionalData(entity, withReferences, save, context));
      entity.setCustomer(buildCustomer(entity, withReferences, save, context));
    }

    // Save the entity
    if (save) {
      entity = context.saveWithDuplicateCheck(entity, this);
    }

    // Build inverse referenced objects
    if (withReferences) {
      List<AuthorEntity> authors = buildAuthors(withReferences, save, context);
      Set<BookAuthorEntity> bookAuthors = buildBookAuthors(authors, entity);
      authors.forEach(book -> book.setBookAuthors(bookAuthors));
      entity.setBookAuthors(bookAuthors);
      if (save) {
        bookAuthors.forEach(context::save);
      }
    }

    return entity;
  }

  private AdditionalBookDataEntity buildAdditionalData(
      final BookEntity entity,
      final boolean withReferences,
      final boolean save,
      final SaveContext context) {
    if (entity.getAdditionalData() != null) {
      return entity.getAdditionalData();
    }
    AdditionalBookDataEntity referencedEntity = null;
    if (this.additionalDataTestDataProvider != null) {
      this.additionalDataTestDataProvider.withBook(entity);
      referencedEntity = this.additionalDataTestDataProvider.buildInternally(withReferences, save, context);
    }
    return referencedEntity;
  }

  private CustomerEntity buildCustomer(
      final BookEntity entity,
      final boolean withReferences,
      final boolean save,
      final SaveContext context) {
    if (entity.getCustomer() != null) {
      return entity.getCustomer();
    }
    CustomerEntity referencedEntity = null;
    if (this.customerTestDataProvider != null) {
      this.customerTestDataProvider.withLoanedBook(entity);
      referencedEntity = this.customerTestDataProvider.buildInternally(withReferences, save, context);
    }
    return referencedEntity;
  }

  private Set<BookAuthorEntity> buildBookAuthors(final List<AuthorEntity> authors, final BookEntity book) {
    return authors.stream()
        .map(author -> BookAuthorEntity.builder()
            .withBook(book)
            .withAuthor(author)
            .build()
            .generateId())
        .collect(Collectors.toSet());
  }

  private List<AuthorEntity> buildAuthors(
      final boolean withReferences,
      final boolean save,
      final SaveContext context) {
    return this.authorTestDataProviders.stream()
        .map(provider -> {
          return provider.buildInternally(withReferences, save, context);
        })
        .toList();
  }
}