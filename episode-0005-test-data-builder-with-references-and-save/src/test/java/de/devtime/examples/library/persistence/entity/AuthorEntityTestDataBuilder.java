package de.devtime.examples.library.persistence.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import de.devtime.examples.library.persistence.entity.AuthorEntity.AuthorEntityBuilder;
import de.devtime.examples.library.test.builder.SaveContext;
import de.devtime.examples.library.test.builder.TestDataBuilder;
import de.devtime.examples.library.test.builder.TestDataBuilderWithSaveSupport;

public class AuthorEntityTestDataBuilder<B extends TestDataBuilder<AuthorEntity>>
    extends AuthorEntityBuilder<B>
    implements TestDataBuilderWithSaveSupport<AuthorEntity> {

  // --------------------< Add referenced builder here >--------------------

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

  // --------------------< Internal builder logic >--------------------

  @Override
  public AuthorEntity buildInternally(
      final boolean withReferences,
      final boolean save,
      final SaveContext context) {
    AuthorEntity entity = build();
    // If the ID was not set via builder configuration, we have to generate a new ID
    if (entity.getId() == null) {
      entity.generateId();
    }

    // Build referenced objects
    // None available

    // Save the entity
    if (save) {
      context.save(entity);
    }

    // Build inverse referenced objects
    if (withReferences) {
      List<BookEntity> books = buildBooks(withReferences, save, context);
      Set<BookAuthorEntity> bookPublishers = buildBookPublishers(books, entity);
      books.forEach(book -> book.setBookAuthors(bookPublishers));
      entity.setBookAuthors(bookPublishers);
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

  private Set<BookAuthorEntity> buildBookPublishers(final List<BookEntity> books, final AuthorEntity publisher) {
    return books.stream()
        .map(book -> BookAuthorEntity.builder()
            .withBook(book)
            .withAuthor(publisher)
            .build()
            .generateId())
        .collect(Collectors.toSet());
  }
}