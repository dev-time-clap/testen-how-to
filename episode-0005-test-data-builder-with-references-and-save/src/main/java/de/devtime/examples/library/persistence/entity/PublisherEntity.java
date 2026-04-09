package de.devtime.examples.library.persistence.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@Getter
@Setter

@Entity
@Table(name = "Publisher")
public class PublisherEntity extends AbstractEntity<PublisherEntity> {

  @Column(name = "NAME")
  private String name;

  // Inverse link
  @OneToMany(mappedBy = "publisher")
  @ToString.Exclude
  @Setter(AccessLevel.PACKAGE)
  private Set<BookPublisherEntity> bookPublishers = new HashSet<>();

  //--------------------< Handle access and bi-directional relationships >--------------------

  public Set<BookPublisherEntity> getBookPublishers() {
    return Collections.unmodifiableSet(this.bookPublishers);
  }

  public void addBookPublisher(final BookPublisherEntity bookPublisher) {
    Objects.requireNonNull(bookPublisher);

    // Avoid endless loops
    if (this.bookPublishers.contains(bookPublisher)) {
      log.debug("The book-publisher relation {} for publisher {} already exists.", bookPublisher, this);
      return;
    }

    // Apply new foreign link
    this.bookPublishers.add(bookPublisher);
  }

  public void removeBookPublisher(final BookPublisherEntity bookPublisher) {
    Objects.requireNonNull(bookPublisher);

    // Avoid endless loops
    if (!this.bookPublishers.remove(bookPublisher)) {
      log.debug("The book-publisher relation {} for publisher {} does not exist.", bookPublisher, this);
      return;
    }

    // Remove the foreign link
    this.bookPublishers.remove(bookPublisher);
  }

  //--------------------< Builder-Pattern Support >--------------------

  @Builder(setterPrefix = "with", toBuilder = true)
  private PublisherEntity(
      final UUID id,
      final int version,
      final String name,
      final Set<BookPublisherEntity> bookPublishers) {
    // Simple fields
    super(id, version, false);
    this.name = name;

    // Referenced entities
    this.bookPublishers = bookPublishers == null ? new HashSet<>() : bookPublishers;
  }

  public static class PublisherEntityBuilder<B> implements GenericBuilder<B> {
    protected PublisherEntityBuilder() {}
  }
}
