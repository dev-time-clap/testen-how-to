package de.devtime.examples.library.test.builder;

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

  @Getter
  private final ApplicationContext applicationContext;

  public boolean isSaveSupported() {
    return this.applicationContext != null;
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