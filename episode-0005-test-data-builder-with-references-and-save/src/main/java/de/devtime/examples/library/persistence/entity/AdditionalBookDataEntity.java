package de.devtime.examples.library.persistence.entity;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
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
@Table(name = "ADDITIONAL_BOOK_DATA")
public class AdditionalBookDataEntity extends AbstractEntity<AdditionalBookDataEntity> {

  @Column(name = "KEYWORDS")
  private String keywords;

  @Column(name = "LANGUAGE_CODE")
  private String languageCode;

  @Column(name = "PAGE_COUNT")
  private Integer pageCount;

  @Column(name = "RATING")
  private Integer rating;

  @Column(name = "SUMMARY", columnDefinition = "TEXT")
  private String summary;

  //--------------------< Bi-directional links >--------------------

  // Inverse link: A book detail always belongs to one book
  @OneToOne(mappedBy = "additionalData")
  @ToString.Exclude
  @Setter(AccessLevel.NONE)
  private BookEntity book;

  public void setBook(final BookEntity book) {
    // Avoid endless loops
    if (Objects.equals(this.book, book)) {
      log.debug("The book {} of the additional book data {} already exist.", book, this);
      return;
    }

    // Apply new foreign link
    BookEntity oldBook = this.book;
    this.book = book;

    // Remove old inverse link
    if (oldBook != null) {
      oldBook.setAdditionalData(null);
    }

    // Apply new inverse link
    if (book != null) {
      book.setAdditionalData(this);
    }
  }

  //--------------------< Builder-Pattern Support >--------------------

  @Builder(setterPrefix = "with", toBuilder = true)
  private AdditionalBookDataEntity(
      final UUID id,
      final int version,
      final String keywords,
      final String languageCode,
      final Integer pageCount,
      final Integer rating,
      final String summary,
      final BookEntity book) {
    // Simple fields
    super(id, version, false);
    this.keywords = keywords;
    this.languageCode = languageCode;
    this.pageCount = pageCount;
    this.rating = rating;
    this.summary = summary;

    // Referenced entities
    this.book = book;
  }

  public static class AdditionalBookDataEntityBuilder<B> implements GenericBuilder<B> {
    protected AdditionalBookDataEntityBuilder() {}
  }
}