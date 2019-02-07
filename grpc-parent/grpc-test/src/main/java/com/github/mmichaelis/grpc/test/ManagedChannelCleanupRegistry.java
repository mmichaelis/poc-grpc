package com.github.mmichaelis.grpc.test;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.grpc.ManagedChannel;

import java.util.concurrent.TimeUnit;

@DefaultAnnotation(NonNull.class)
public class ManagedChannelCleanupRegistry extends ResourceCleanupRegistry<ManagedChannel> {
  ManagedChannelCleanupRegistry(long timeout, TimeUnit timeoutUnit) {
    super(timeout, timeoutUnit);
  }

  @Override
  protected Resource<ManagedChannel> wrap(ManagedChannel service) {
    return new ManagedChannelResource(service);
  }
}
