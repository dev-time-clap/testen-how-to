package de.devtime.examples.library.api.contract.book;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(setterPrefix = "with")
@Data
public class BookRegistrationDto {

  // Fachliche Daten
  private String isbn;
  private String title;

  // Technische Referenzen bei Auswahl
  private UUID authorId;
  private UUID publisherId;

}
