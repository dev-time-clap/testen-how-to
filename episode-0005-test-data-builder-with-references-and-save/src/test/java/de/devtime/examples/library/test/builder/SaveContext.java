package de.devtime.examples.library.test.builder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class SaveContext {

  private static String cacheKey(final Class<?> entityClass, final String uniqueKey) {
    return entityClass.getName() + "::" + uniqueKey;
  }

  @Getter
  private final ApplicationContext applicationContext;

  private final Map<String, Object> cache = new HashMap<>();

  public boolean isSaveSupported() {
    return this.applicationContext != null;
  }

  public void put(final Class<?> entityClass, final String uniqueKey, final Object entity) {
    this.cache.put(cacheKey(entityClass, uniqueKey), entity);
  }

  public boolean contains(final Class<?> entityClass, final String uniqueKey) {
    return this.cache.containsKey(cacheKey(entityClass, uniqueKey));
  }

  @SuppressWarnings("unchecked")
  public <E> E get(final Class<?> entityClass, final String uniqueKey) {
    return (E) this.cache.get(cacheKey(entityClass, uniqueKey));
  }

  public void clear() {
    log.trace("Clear save context");
    this.cache.clear();
  }

  public <E> E saveWithDuplicateCheck(final E entity, final TestDataBuilderWithSaveSupport<E> builder) {
    if (isSaveSupported()) {
      String uniqueTestDataSetKey = builder.getUniqueTestDataSetKey(entity);
      if (contains(entity.getClass(), uniqueTestDataSetKey)) {
        return get(entity.getClass(), uniqueTestDataSetKey);
      } else {
        EntityManager entityManager = this.applicationContext.getBean(EntityManager.class);
        entityManager.persist(entity);
        put(entity.getClass(), uniqueTestDataSetKey, entity);
        return entity;
      }
    } else {
      return entity;
    }
  }

  public <E> E save(final E entity) {
    if (isSaveSupported()) {
      EntityManager entityManager = this.applicationContext.getBean(EntityManager.class);
      entityManager.persist(entity);
      return entity;
    } else {
      return entity;
    }
  }
}