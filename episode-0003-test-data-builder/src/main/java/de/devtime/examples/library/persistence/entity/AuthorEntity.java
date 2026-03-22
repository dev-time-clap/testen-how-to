package de.devtime.examples.library.persistence.entity;

import java.time.LocalDate;

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
@Table(name = "Author")
public class AuthorEntity extends AbstractEntity<AuthorEntity> {

  @Column(name = "FIRST_NAME")
  private String firstName;

  @Column(name = "LAST_NAME")
  private String lastName;

  @Column(name = "ARTIST_NAME", nullable = false)
  private String artistName;

  @Column(name = "BIRTHDAY")
  private LocalDate birthday;

  //--------------------< Builder-Pattern Support >--------------------

  public static class AuthorEntityBuilder<B> implements GenericBuilder<B> {
    protected AuthorEntityBuilder() {}
  }
}
