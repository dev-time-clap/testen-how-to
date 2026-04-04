package de.devtime.examples.library.persistence.entity;

import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@AllArgsConstructor
@Builder(setterPrefix = "with")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@Getter
@Setter

@Entity
@Table(name = "RATING")
public class RatingEntity extends AbstractEntity<RatingEntity> {

  @Column(name = "STARS")
  private int stars;

  @Column(name = "TOTAL_COUNT")
  private int totalCount;

  //--------------------< Bi-directional links >--------------------

  // Primary link: A rating belongs to one additional book data
  @ManyToOne
  @JoinColumn(name = "ADDITIONAL_BOOK_DATA_ID")
  @Setter(AccessLevel.NONE)
  private AdditionalBookDataEntity additionalBookData;

  public void setAdditionalBookData(final AdditionalBookDataEntity additionalBookData) {
    // Avoid endless loops
    if (Objects.equals(this.additionalBookData, additionalBookData)) {
      log.debug("The additional book data {} of the rating {} already exist.", additionalBookData, this);
      return;
    }

    // Remove old inverse link
    if (this.additionalBookData != null) {
      this.additionalBookData.removeRating(this);
    }

    // Apply new primary link
    this.additionalBookData = additionalBookData;

    // Apply new inverse link
    if (additionalBookData != null && !additionalBookData.getRatings().contains(this)) {
      additionalBookData.addRating(this);
    }
  }

  //--------------------< Builder-Pattern Support >--------------------

  @Builder(setterPrefix = "with", toBuilder = true)
  private RatingEntity(final UUID id,
      final int version,
      final int stars,
      final int totalCount,
      final AdditionalBookDataEntity additionalBookData) {
    super(id, version, false);
    this.stars = stars;
    this.totalCount = totalCount;
    this.additionalBookData = additionalBookData;
  }

  public static class RatingEntityBuilder<B> implements GenericBuilder<B> {
    protected RatingEntityBuilder() {}
  }
}
