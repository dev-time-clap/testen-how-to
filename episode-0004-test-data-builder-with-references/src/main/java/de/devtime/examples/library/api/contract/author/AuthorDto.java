package de.devtime.examples.library.api.contract.author;

import java.time.LocalDate;
import java.util.UUID;

import de.devtime.examples.library.api.contract.AbstractPersistableDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AuthorDto extends AbstractPersistableDto {

  private String firstName;
  private String lastName;
  private String artistName;
  private LocalDate birthday;

  @Builder(setterPrefix = "with")
  private AuthorDto(
      final UUID id,
      final String firstName,
      final String lastName,
      final String artistName,
      final LocalDate birthday) {
    super(id);
    this.firstName = firstName;
    this.lastName = lastName;
    this.artistName = artistName;
    this.birthday = birthday;
  }
}
