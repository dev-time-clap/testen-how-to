package de.devtime.examples.library.test.builder;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

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

  private final Map<String, Object> cache = new HashMap<>();

  @Getter
  private final ApplicationContext applicationContext;

  public void put(final Class<?> entityClass, final String uniqueKey, final Object entity) {
    this.cache.put(cacheKey(entityClass, uniqueKey), entity);
  }

  @SuppressWarnings("unchecked")
  public <E> E get(final Class<?> entityClass, final String uniqueKey) {
    return (E) this.cache.get(cacheKey(entityClass, uniqueKey));
  }

  public boolean contains(final Class<?> entityClass, final String uniqueKey) {
    return this.cache.containsKey(cacheKey(entityClass, uniqueKey));
  }

  public boolean isSaveSupported() {
    return this.applicationContext != null;
  }

  public void clear() {
    log.trace("Clear save context");
    this.cache.clear();
  }
}