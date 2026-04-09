package de.devtime.examples.library.test.builder;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Propagation;

import de.devtime.examples.library.context.ApplicationContextProvider;
import de.devtime.examples.library.persistence.util.TransactionHelper;

public interface TestDataBuilderWithSaveSupport<E>
    extends TestDataBuilder<E> {

  default E buildWithReferencesAndSave() {
    ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    TransactionHelper txHelper = applicationContext.getBean(TransactionHelper.class);
    SaveContext saveContext = applicationContext.getBean(SaveContext.class);
    return txHelper.executeInTx(Propagation.REQUIRES_NEW, _ -> {
      return buildInternally(true, true, saveContext);
    });
  }

  E buildInternally(final boolean withReferences, final boolean save, final SaveContext context);

  @Override
  default E buildInternally(final boolean withReferences) {
    return buildInternally(withReferences, false, null);
  }
}
