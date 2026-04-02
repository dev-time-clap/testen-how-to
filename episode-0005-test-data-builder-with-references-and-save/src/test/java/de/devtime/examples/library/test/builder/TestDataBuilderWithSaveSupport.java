package de.devtime.examples.library.test.builder;

import java.util.UUID;

import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;

import de.devtime.examples.library.context.ApplicationContextProvider;
import de.devtime.examples.library.persistence.util.TransactionHelper;

public interface TestDataBuilderWithSaveSupport<E, R extends JpaRepository<E, UUID>>
    extends TestDataBuilder<E> {

  String getUniqueDataSetKey(E entity);

  R getRepository(final ApplicationContext appContext);

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
      if (saveContext.contains(entity.getClass(), getUniqueDataSetKey(entity))) {
        return saveContext.get(entity.getClass(), getUniqueDataSetKey(entity));
      } else {
        R repository = getRepository(saveContext.getApplicationContext());
        E savedEntity = repository.save(entity);
        saveContext.put(savedEntity.getClass(), getUniqueDataSetKey(savedEntity), savedEntity);
        return savedEntity;
      }
    } else {
      return entity;
    }
  }
}
