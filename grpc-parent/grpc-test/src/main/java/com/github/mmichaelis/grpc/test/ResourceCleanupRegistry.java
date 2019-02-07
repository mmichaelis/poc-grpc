package com.github.mmichaelis.grpc.test;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Cleanup Registry for Servers.
 */
@DefaultAnnotation(NonNull.class)
public abstract class ResourceCleanupRegistry<T> implements CleanupRegistry<T> {
  private final Deque<Resource<T>> servers = new LinkedList<>();

  private final long timeout;
  private final TimeUnit timeoutUnit;

  ResourceCleanupRegistry(long timeout, TimeUnit timeoutUnit) {
    this.timeout = timeout;
    this.timeoutUnit = timeoutUnit;
  }

  protected abstract Resource<T> wrap(T service);

  @Override
  public <R extends T> R register(R cleanupItem) {
    servers.push(wrap(cleanupItem));
    return cleanupItem;
  }

  @Override
  public synchronized void clear() {
    shutdownAll();
    Optional<Resource<T>> firstFailed = awaitTermination();
    servers.clear();
    if (firstFailed.isPresent()) {
      throw new CleanupException("Failed to shutdown " + firstFailed.get() + " within given timeout.");
    }
  }

  private void shutdownAll() {
    for (Resource<?> server : servers) {
      server.shutdown();
    }
  }

  private Optional<Resource<T>> awaitTermination() {
    Optional<Resource<T>> firstFailed = servers.parallelStream()
            .filter(s -> awaitTermination(s) != TerminationState.SUCCESS)
            .findFirst();

    if (firstFailed.isPresent()) {
      // If any failed, force shutdown.
      servers.parallelStream().forEach(Resource::shutdownNow);
    }
    return firstFailed;
  }

  private TerminationState awaitTermination(Resource<?> server) {
    try {
      if (server.awaitTermination(timeout, timeoutUnit)) {
        return TerminationState.SUCCESS;
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      return TerminationState.INTERRUPTED;
    }
    return TerminationState.FAILURE;
  }

}
