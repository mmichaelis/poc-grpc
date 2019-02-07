package com.github.mmichaelis.grpc.test;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.grpc.ManagedChannel;

import java.util.concurrent.TimeUnit;

@DefaultAnnotation(NonNull.class)
class ManagedChannelResource extends Resource<ManagedChannel> {
  ManagedChannelResource(ManagedChannel delegate) {
    super(delegate);
  }

  @Override
  void shutdown() {
    getDelegate().shutdown();
  }

  @Override
  void shutdownNow() {
    getDelegate().shutdownNow();
  }

  @Override
  boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
    return getDelegate().awaitTermination(timeout, unit);
  }
}
