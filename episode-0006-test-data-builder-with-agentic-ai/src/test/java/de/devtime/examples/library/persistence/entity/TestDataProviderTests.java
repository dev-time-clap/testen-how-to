package de.devtime.examples.library.persistence.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class TestDataProviderTests {

  @Test
  void testBookEntityTestDataProvider() {
    BookEntity entity = BookEntityTestDataProvider.create()
        .bookByMorriganWithTitleTestingWithJUnitAndCo()
        .withCustomer(CustomerEntityTestDataProvider::customerErikaMustermann)
        .withIsbn("ISBN-0817")
        .and()
        .buildWithReferencesAndSave();

    log.info("entity: {}", entity);
    assertThat(entity).isNotNull();
    assertThat(entity.getId()).isNotNull();
    assertThat(entity.getVersion()).isOne();
    assertThat(entity.getIsbn()).isEqualTo("ISBN-0817");
    assertThat(entity.getTitle()).isEqualTo("Testing with JUnit, Spring & Co.");
    assertThat(entity.isOnLoan()).isFalse();
  }

  @Test
  void testAdditionalBookDataEntityTestDataProvider() {
    AdditionalBookDataEntity entity = AdditionalBookDataEntityTestDataProvider.create()
        .bookDetailsForTestingWithJUnitAndCoByMorrigan()
        .buildWithReferencesAndSave();

    log.info("entity: {}", entity);
    assertThat(entity).isNotNull();
    assertThat(entity.getBook()).isNotNull();
    assertThat(entity.getSummary()).isEqualTo("Ein lehrreiches Buch über Softwareentwicklung.");
    assertThat(entity.getRating()).isEqualTo(5);
    assertThat(entity.getPageCount()).isEqualTo(321);
    assertThat(entity.getLanguageCode()).isEqualTo("DE");
    assertThat(entity.getKeywords()).isEqualTo("Wissen, Softwareentwicklung, Lehrbuch");
  }

  @Test
  void testCustomerEntityTestDataProvider() {
    CustomerEntity entity = CustomerEntityTestDataProvider.create()
        .customerErikaMustermann()
        .withLoanedBook(BookEntityTestDataProvider::bookByMorriganWithTitleTestingWithJUnitAndCo)
        .withLoanedBook(BookEntityTestDataProvider::bookByMorriganWithTitleLombokHowTo)
        .withLoanedBook(BookEntityTestDataProvider::bookByMorriganWithTitleSpringBootPrototyping)
        .buildWithReferencesAndSave();

    log.info("entity: {}", entity);
    assertThat(entity).isNotNull();
    assertThat(entity.getLoanedBooks()).hasSize(3);
    assertThat(entity.getFirstName()).isEqualTo("Erika");
    assertThat(entity.getLastName()).isEqualTo("Mustermann");
    assertThat(entity.getNumber()).isEqualTo("knd-0002");

    entity = CustomerEntityTestDataProvider.create()
        .customerMaxMustermann()
        .buildWithReferencesAndSave();

    log.info("entity: {}", entity);
    assertThat(entity).isNotNull();
    assertThat(entity.getLoanedBooks()).hasSize(1);
    assertThat(entity.getFirstName()).isEqualTo("Max");
    assertThat(entity.getLastName()).isEqualTo("Mustermann");
    assertThat(entity.getNumber()).isEqualTo("knd-0001");
  }

}
