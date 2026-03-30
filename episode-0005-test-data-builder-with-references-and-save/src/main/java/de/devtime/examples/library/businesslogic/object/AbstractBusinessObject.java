package de.devtime.examples.library.businesslogic.object;

public abstract class AbstractBusinessObject<T> {

  @SuppressWarnings("unchecked")
  protected T self() {
    return (T) this;
  }
}
