package de.devtime.examples.library.test.builder;

public interface TestDataBuilder<E> {

  E buildInternally(final boolean withReferences);

  default E buildOnlyThis() {
    return buildInternally(false);
  }

  default E buildWithReferences() {
    return buildInternally(true);
  }
}
