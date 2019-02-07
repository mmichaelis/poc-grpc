package com.github.mmichaelis.grpc.test;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.concurrent.TimeUnit;

/**
 * Generic wrapper for stoppable resources.
 *
 * @param <T> resource type
 */
@DefaultAnnotation(NonNull.class)
abstract class Resource<T> {
  private final T delegate;

  Resource(T delegate) {
    this.delegate = delegate;
  }

  T getDelegate() {
    return delegate;
  }

  abstract void shutdown();

  abstract void shutdownNow();

  abstract boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

  @Override
  public String toString() {
    return delegate.toString();
  }
}
