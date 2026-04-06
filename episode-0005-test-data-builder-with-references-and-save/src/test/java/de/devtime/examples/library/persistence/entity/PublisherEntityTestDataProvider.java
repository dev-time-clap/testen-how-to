package de.devtime.examples.library.persistence.entity;

public class PublisherEntityTestDataProvider extends PublisherEntityTestDataBuilder<PublisherEntityTestDataProvider> {

  public static PublisherEntityTestDataProvider create() {
    return new PublisherEntityTestDataProvider();
  }

  public PublisherEntityTestDataProvider publisherTempelMedia() {
    withName("TempelMedia GmbH");
    return and();
  }

}
