package de.devtime.examples.library.test.builder;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Propagation;

import de.devtime.examples.library.context.ApplicationContextProvider;
import de.devtime.examples.library.persistence.util.TransactionHelper;
import jakarta.persistence.EntityManager;

public interface TestDataBuilderWithSaveSupport<E>
    extends TestDataBuilder<E> {

  String getUniqueTestDataSetKey(E entity);

  E buildInternally(final boolean withReferences, final boolean save, final SaveContext context);

  default E buildWithReferencesAndSave() {
    ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    TransactionHelper txHelper = applicationContext.getBean(TransactionHelper.class);
    SaveContext saveContext = applicationContext.getBean(SaveContext.class);
    return txHelper.executeInTx(Propagation.REQUIRES_NEW, _ -> {
      return buildInternally(true, true, saveContext);
    });
  }

  @Override
  default E buildInternally(final boolean withReferences) {
    return buildInternally(withReferences, false, null);
  }

  default E save(final E entity, final SaveContext saveContext) {
    if (saveContext != null && saveContext.isSaveSupported()) {
      if (saveContext.contains(entity.getClass(), getUniqueTestDataSetKey(entity))) {
        return saveContext.get(entity.getClass(), getUniqueTestDataSetKey(entity));
      } else {
        EntityManager entityManager = saveContext.getApplicationContext().getBean(EntityManager.class);
        entityManager.persist(entity);
        saveContext.put(entity.getClass(), getUniqueTestDataSetKey(entity), entity);
        return entity;
      }
    } else {
      return entity;
    }
  }
}
