package de.devtime.examples.library.api.contract.publisher;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(setterPrefix = "with")
@Data
public class PublisherRegistrationDto {

  // Fachliche Daten
  private String name;

}
