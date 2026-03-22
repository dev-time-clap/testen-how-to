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
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(setterPrefix = "with")
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
@Getter
@Setter

@Entity
@Table(name = "Book")
public class BookEntity extends AbstractEntity<BookEntity> {

  @Column(name = "ISBN", nullable = false)
  private String isbn;

  @Column(name = "TITLE", nullable = false)
  private String title;

  @Column(name = "IS_ON_LOAN")
  private boolean isOnLoan;

  //--------------------< Builder-Pattern Support >--------------------

  public static class BookEntityBuilder<B> implements GenericBuilder<B> {
    protected BookEntityBuilder() {}
  }
}
