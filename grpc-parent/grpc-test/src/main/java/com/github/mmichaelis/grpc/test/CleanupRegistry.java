package com.github.mmichaelis.grpc.test;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Registry for services to clean-up.
 */
@DefaultAnnotation(NonNull.class)
public interface CleanupRegistry<T> {
  /**
   * Register item to clean-up at end of test.
   *
   * @param cleanupItem item to cleanup
   * @param <R>         type of the item
   * @return the registered item
   */
  <R extends T> R register(R cleanupItem);

  /**
   * Stops all registered services and removes them from the registry.
   */
  void clear();
}
