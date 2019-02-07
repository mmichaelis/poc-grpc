package com.github.mmichaelis.grpc.test;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.grpc.Server;

import java.util.concurrent.TimeUnit;

@DefaultAnnotation(NonNull.class)
public class ServerCleanupRegistry extends ResourceCleanupRegistry<Server> {
  ServerCleanupRegistry(long timeout, TimeUnit timeoutUnit) {
    super(timeout, timeoutUnit);
  }

  @Override
  protected Resource<Server> wrap(Server service) {
    return new ServerResource(service);
  }
}
