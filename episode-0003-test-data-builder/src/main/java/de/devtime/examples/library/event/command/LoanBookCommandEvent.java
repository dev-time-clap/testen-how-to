package de.devtime.examples.library.event.command;

import de.devtime.examples.library.api.contract.book.BookLoanDto;
import de.devtime.examples.library.context.AbstractManualAutowiredBean;
import de.devtime.examples.library.event.AbstractEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder(setterPrefix = "with")
public class LoanBookCommandEvent extends AbstractEvent {

  @Getter
  private BookLoanDto bookLoan;

  public static class LoanBookCommandEventBuilder
      extends AbstractManualAutowiredBean<LoanBookCommandEventBuilder> {

    public void fire() {
      LoanBookCommandEvent eventToFire = build();
      log.info("Fire event: {}", eventToFire.getClass().getSimpleName());
      this.appContext.publishEvent(eventToFire);
    }
  }

}
