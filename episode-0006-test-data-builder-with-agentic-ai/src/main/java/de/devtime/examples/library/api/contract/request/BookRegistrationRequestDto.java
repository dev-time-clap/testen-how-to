package de.devtime.examples.library.api.contract.request;

import de.devtime.examples.library.api.contract.author.AuthorRegistrationDto;
import de.devtime.examples.library.api.contract.book.BookRegistrationDto;
import de.devtime.examples.library.api.contract.publisher.PublisherRegistrationDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Builder(setterPrefix = "with")
@Data
public class BookRegistrationRequestDto {

  private BookRegistrationDto book;
  private AuthorRegistrationDto author;
  private PublisherRegistrationDto publisher;

}
