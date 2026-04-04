package de.devtime.examples.library.api.impl;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import de.devtime.examples.library.api.contract.EndpointConstants;
import de.devtime.examples.library.api.contract.book.BookLoanDto;
import de.devtime.examples.library.api.contract.request.BookRegistrationRequestDto;
import de.devtime.examples.library.event.command.LoanBookCommandEvent;
import de.devtime.examples.library.event.command.RegisterBookCommandEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BookRestController {

  @PostMapping(EndpointConstants.PATH_BOOKS_REGISTRATION)
  @ResponseStatus(HttpStatus.CREATED)
  public void registerBook(@RequestBody final BookRegistrationRequestDto requestDto) {
    RegisterBookCommandEvent.builder()
        .withBook(requestDto.getBook())
        .withAuthor(requestDto.getAuthor())
        .withPublisher(requestDto.getPublisher())
        .fire();
  }

  @Operation(
      summary = "Verleiht ein Buch an einen Kunden.",
      tags = "Bücher",
      description = "<h1>Verleiht ein Buch an einen Kunden</h1>"
          + "<h2>Beschreibung</h2>"
          + "Über diese Schnittstelle wird ein Buch an einen Kunden ausgeliehen.<br>"
          + "Das Buch wird über dessen eindeutige ISBN Nummer identifiziert.<br>"
          + "Der Kunde wird über dessen internen Identifier angegeben.<br>"
          + "<br>"
          + "<h2>Plausibilisierungen</h2>"
          + "<ul>"
          + "<li>Der Kunde muss im System registriert sein.</li>"
          + "<li>Das Buch mit der angegebenen ISBN muss im System registriert sein.</li>"
          + "<li>Das Buch darf nicht bereits ausgeliehen sein.</li>"
          + "</ul>",
      requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
          content = @Content(
              mediaType = org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = BookLoanDto.class,
                  example = """
                      {
                        "isbn" : "ISBN-0815",
                        "customerId" : "3fa85f64-5717-4562-b3fc-2c963f66afa6"
                      }
                      """))),
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Das Buch wurde an den Kunden ausgeliehen."),
          @ApiResponse(
              responseCode = "400",
              description = "Das Buch konnte nicht an den Kunden ausgeliehen werden. Details zu dem Grund befinden sich in der Antwort. "),
          @ApiResponse(
              responseCode = "409",
              description = "Das Buch konnte nicht an den Kunden ausgeliehen werden, weil es bereits verliehen ist.")

      })
  @PostMapping(EndpointConstants.PATH_BOOKS_LOAN)
  @ResponseStatus(HttpStatus.OK)
  public void loanOutBook(@RequestBody final BookLoanDto requestDto) {
    LoanBookCommandEvent.builder()
        .withBookLoan(requestDto)
        .fire();
  }
}
