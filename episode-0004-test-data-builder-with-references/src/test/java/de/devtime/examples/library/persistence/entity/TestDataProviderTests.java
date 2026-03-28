package de.devtime.examples.library.persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TestDataProviderTests {

  @Test
  void testBookEntityTestDataProvider() {
    BookEntity entity = BookEntityTestDataProvider.create()
        .bookByMorriganWithTitleTestingWithJUnitAndCo()
        .withIsbn("ISBN-0817")
        .and()
        .buildOnlyThis();

    assertThat(entity).isNotNull();
    assertThat(entity.getId()).isNotNull();
    assertThat(entity.getVersion()).isZero();
    assertThat(entity.getIsbn()).isEqualTo("ISBN-0816");
    assertThat(entity.getTitle()).isEqualTo("Testing with JUnit, Spring & Co.");
    assertThat(entity.isOnLoan()).isFalse();
  }

}
