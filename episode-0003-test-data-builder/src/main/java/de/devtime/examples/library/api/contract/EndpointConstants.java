package de.devtime.examples.library.api.contract;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointConstants {

  public static final String PATH_BOOKS_REGISTRATION = "/api/books/registration";
  public static final String PATH_BOOKS_LOAN = "/api/books/loan";

}
