package de.devtime.examples.library.persistence.entity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(setterPrefix = "with")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@Getter
@Setter

@Entity
@Table(name = "Publisher")
public class PublisherEntity extends AbstractEntity<PublisherEntity> {

  @Column(name = "NAME")
  private String name;

  //--------------------< Bi-directional links >--------------------

  @OneToMany(mappedBy = "publisher")
  @Builder.Default
  @ToString.Exclude
  @Setter(AccessLevel.NONE)
  private final Set<BookPublisherEntity> bookPublishers = new HashSet<>();

  public void addBookPublisher(final BookPublisherEntity bookPublisher) {
    Objects.requireNonNull(bookPublisher);

    // Avoid endless loops
    if (this.bookPublishers.contains(bookPublisher)) {
      log.debug("The book-publisher relation {} for publisher {} already exists.", bookPublisher, this);
      return;
    }

    this.bookPublishers.add(bookPublisher);

    // Apply inverse link
    bookPublisher.setPublisher(this);
  }

  //--------------------< Builder-Pattern Support >--------------------

  public static class PublisherEntityBuilder<B> implements GenericBuilder<B> {
    protected PublisherEntityBuilder() {}
  }
}
