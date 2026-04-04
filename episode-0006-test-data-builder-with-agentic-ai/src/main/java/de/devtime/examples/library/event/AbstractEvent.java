package de.devtime.examples.library.event;

import lombok.Getter;

public abstract class AbstractEvent {

  @Getter
  private final long timestamp;

  protected AbstractEvent() {
    super();
    this.timestamp = System.currentTimeMillis();
  }
}
