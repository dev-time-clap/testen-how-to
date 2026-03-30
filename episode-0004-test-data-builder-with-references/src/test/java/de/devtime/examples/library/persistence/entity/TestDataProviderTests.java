package de.devtime.examples.library.persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class TestDataProviderTests {

  @Test
  void testBookEntityTestDataProvider() {
    BookEntity entity = BookEntityTestDataProvider.create()
        .bookByMorriganWithTitleTestingWithJUnitAndCo()
        .withAdditionalData(AdditionalBookDataEntityTestDataProvider::bookDetailsForTestingWithJUnitAndCoByMorrigan)
        .withCustomer(CustomerEntityTestDataProvider::customerErikaMustermann)
        .withIsbn("ISBN-0817")
        .and()
        .buildWithReferences();

    log.info("entity: {}", entity);
    assertThat(entity).isNotNull();
    assertThat(entity.getId()).isNotNull();
    assertThat(entity.getVersion()).isZero();
    assertThat(entity.getIsbn()).isEqualTo("ISBN-0817");
    assertThat(entity.getTitle()).isEqualTo("Testing with JUnit, Spring & Co.");
    assertThat(entity.isOnLoan()).isFalse();
  }

}
