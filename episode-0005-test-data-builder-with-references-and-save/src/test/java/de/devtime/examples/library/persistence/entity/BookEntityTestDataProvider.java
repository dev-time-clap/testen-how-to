package de.devtime.examples.library.persistence.entity;

public class BookEntityTestDataProvider extends BookEntityTestDataBuilder<BookEntityTestDataProvider> {

  public static BookEntityTestDataProvider create() {
    return new BookEntityTestDataProvider();
  }

  public BookEntityTestDataProvider bookByMorriganWithTitleLombokHowTo() {
    withIsbn("ISBN-0815");
    withIsOnLoan(false);
    withTitle("Lombok - How to");
    return and();
  }

  public BookEntityTestDataProvider bookByMorriganWithTitleTestingWithJUnitAndCo() {
    withAdditionalData(AdditionalBookDataEntityTestDataProvider::bookDetailsForTestingWithJUnitAndCoByMorrigan);
    withIsbn("ISBN-0816");
    withIsOnLoan(false);
    withTitle("Testing with JUnit, Spring & Co.");
    return and();
  }

  public BookEntityTestDataProvider bookByMorriganWithTitleSpringBootPrototyping() {
    withIsbn("ISBN-0817");
    withIsOnLoan(false);
    withTitle("Spring Boot Prototyping");
    return and();
  }

}
