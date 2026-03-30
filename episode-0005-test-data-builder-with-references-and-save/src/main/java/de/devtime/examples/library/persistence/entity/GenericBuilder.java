package de.devtime.examples.library.persistence.entity;

public interface GenericBuilder<B> {

  @SuppressWarnings("unchecked")
  default B and() {
    return (B) this;
  }
}
