package de.devtime.examples.library.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

  //--------------------< Builder-Pattern Support >--------------------

  public static class AdditionalBookDataEntityBuilder<B> implements GenericBuilder<B> {
    protected AdditionalBookDataEntityBuilder() {}
  }
}