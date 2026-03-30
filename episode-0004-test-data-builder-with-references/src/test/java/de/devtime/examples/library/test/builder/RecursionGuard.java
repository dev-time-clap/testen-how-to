package de.devtime.examples.library.test.builder;

import java.util.HashSet;
import java.util.Set;

public class RecursionGuard {
  private static final ThreadLocal<Set<Class<?>>> CLASSES = ThreadLocal.withInitial(HashSet::new);

  public static void guard(final Class<?> providerClass, final Runnable action) {
    if (CLASSES.get().contains(providerClass)) {
      return; // skip recursion
    }

    try {
      CLASSES.get().add(providerClass);
      action.run();
    } finally {
      CLASSES.get().remove(providerClass);
    }
  }
}
