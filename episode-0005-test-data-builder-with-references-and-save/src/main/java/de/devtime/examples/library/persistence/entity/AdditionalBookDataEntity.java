package de.devtime.examples.library.persistence.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
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
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(setterPrefix = "with")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@Getter
@Setter

@Entity
@Table(name = "ADDITIONAL_BOOK_DATA")
public class AdditionalBookDataEntity extends AbstractEntity<AdditionalBookDataEntity> {

  @Column(name = "SUMMARY", columnDefinition = "TEXT")
  private String summary;

  @Column(name = "RATING")
  private Integer rating;

  @Column(name = "PAGE_COUNT")
  private Integer pageCount;

  @Column(name = "LANGUAGE_CODE")
  private String languageCode;

  @Column(name = "KEYWORDS")
  private String keywords;

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

    this.book = book;

    // Apply bi-directional link
    if (book != null) {
      book.setAdditionalData(this);
    }
  }

  //--------------------< Builder-Pattern Support >--------------------

  public static class AdditionalBookDataEntityBuilder<B> implements GenericBuilder<B> {
    protected AdditionalBookDataEntityBuilder() {}
  }
}