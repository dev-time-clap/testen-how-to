package de.devtime.examples.library.api.contract.author;

import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(setterPrefix = "with")
@Data
public class AuthorRegistrationDto {

  // Fachliche Daten
  private String firstName;
  private String lastName;
  private String artistName;
  private LocalDate birthday;

}
