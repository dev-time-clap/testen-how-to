package de.devtime.examples.library.persistence.entity;

public class AdditionalBookDataEntityTestDataProvider
    extends AdditionalBookDataEntityTestDataBuilder<AdditionalBookDataEntityTestDataProvider> {

  public static AdditionalBookDataEntityTestDataProvider create() {
    return new AdditionalBookDataEntityTestDataProvider();
  }

  public AdditionalBookDataEntityTestDataProvider bookDetailsForTestingWithJUnitAndCoByMorrigan() {
    withBook(BookEntityTestDataProvider::bookByMorriganWithTitleTestingWithJUnitAndCo);
    withSummary("Ein lehrreiches Buch über Softwareentwicklung.");
    withRating(5);
    withPageCount(321);
    withLanguageCode("DE");
    withKeywords("Wissen, Softwareentwicklung, Lehrbuch");
    return and();
  }

  public AdditionalBookDataEntityTestDataProvider bookDetailsWithEmptyDetails() {
    withSummary("");
    withRating(null);
    withPageCount(null);
    return and();
  }
}
