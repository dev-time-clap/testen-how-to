package de.devtime.examples.library.test.builder;

public interface TestDataBuilder<E> {

  E buildInternally();

  default E buildOnlyThis() {
    return buildInternally();
  }
}
