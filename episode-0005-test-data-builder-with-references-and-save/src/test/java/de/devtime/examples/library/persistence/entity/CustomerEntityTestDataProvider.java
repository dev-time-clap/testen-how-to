package de.devtime.examples.library.persistence.entity;

public class CustomerEntityTestDataProvider extends CustomerEntityTestDataBuilder<CustomerEntityTestDataProvider> {

  public static CustomerEntityTestDataProvider create() {
    return new CustomerEntityTestDataProvider();
  }

  public CustomerEntityTestDataProvider customerMaxMustermann() {
    withFirstName("Max");
    withLastName("Mustermann");
    withNumber("knd-0001");
    withLoanedBook(book -> book
        .bookByMorriganWithTitleTestingWithJUnitAndCo()
        .withIsOnLoan(true));
    return and();
  }

  public CustomerEntityTestDataProvider customerErikaMustermann() {
    withFirstName("Erika");
    withLastName("Mustermann");
    withNumber("knd-0002");
    return and();
  }
}