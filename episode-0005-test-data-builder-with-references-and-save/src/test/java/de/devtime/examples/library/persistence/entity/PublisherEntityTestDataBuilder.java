package de.devtime.examples.library.persistence.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.devtime.examples.library.persistence.entity.PublisherEntity.PublisherEntityBuilder;
import de.devtime.examples.library.test.builder.SaveContext;
import de.devtime.examples.library.test.builder.TestDataBuilder;
import de.devtime.examples.library.test.builder.TestDataBuilderWithSaveSupport;

public class PublisherEntityTestDataBuilder<B extends TestDataBuilder<PublisherEntity>>
    extends PublisherEntityBuilder<B>
    implements TestDataBuilderWithSaveSupport<PublisherEntity> {

  // --------------------< Add referenced builder here >---------

  private final List<BookEntityTestDataProvider> bookTestDataProviders = new ArrayList<>();

  public B withBook(final Consumer<BookEntityTestDataProvider> consumer) {
    BookEntityTestDataProvider builder = BookEntityTestDataProvider.create();
    consumer.accept(builder);
    this.bookTestDataProviders.add(builder);
    return and();
  }

  public B withBook(final BookEntityTestDataProvider bookTestDataBuilder) {
    this.bookTestDataProviders.add(bookTestDataBuilder);
    return and();
  }

  // --------------------< Internal builder logic >---------

  @Override
  public String getUniqueTestDataSetKey(final PublisherEntity entity) {
    return entity.getName();
  }

  @Override
  public PublisherEntity buildInternally(
      final boolean withReferences,
      final boolean save,
      final SaveContext context) {
    PublisherEntity entity = build();
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
      List<BookEntity> books = buildBooks(withReferences, save, context);
      Set<BookPublisherEntity> bookPublishers = buildBookPublishers(books, entity);
      books.forEach(book -> book.setBookPublishers(bookPublishers));
      entity.setBookPublishers(bookPublishers);
      if (save) {
        bookPublishers.forEach(context::save);
      }
    }

    return entity;
  }

  private List<BookEntity> buildBooks(
      final boolean withReferences,
      final boolean save,
      final SaveContext context) {
    return this.bookTestDataProviders.stream()
        .map(provider -> {
          return provider.buildInternally(withReferences, save, context);
        })
        .toList();
  }

  private Set<BookPublisherEntity> buildBookPublishers(final List<BookEntity> books, final PublisherEntity publisher) {
    return books.stream()
        .map(book -> BookPublisherEntity.builder()
            .withBook(book)
            .withPublisher(publisher)
            .build()
            .generateId())
        .collect(Collectors.toSet());
  }
}