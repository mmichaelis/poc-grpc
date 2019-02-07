package com.github.mmichaelis.grpc.test;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.util.AnnotationUtils;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@DefaultAnnotation(NonNull.class)
public class GrpcTestExtension implements ParameterResolver, AfterEachCallback {

  private static final long DEFAULT_TIMEOUT_SECONDS = 10L;

  @Override
  public void afterEach(ExtensionContext context) {
    getManagedChannelCleanupRegistry(context).clear();
    getServerCleanupRegistry(context).clear();
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    Class<?> parameterType = parameterContext.getParameter().getType();
    return ServerCleanupRegistry.class.isAssignableFrom(parameterType) || ManagedChannelCleanupRegistry.class.isAssignableFrom(parameterType);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
    Class<?> parameterType = parameterContext.getParameter().getType();
    if (ServerCleanupRegistry.class.isAssignableFrom(parameterType)) {
      return getServerCleanupRegistry(extensionContext);
    }
    if (ManagedChannelCleanupRegistry.class.isAssignableFrom(parameterType)) {
      return getManagedChannelCleanupRegistry(extensionContext);
    }
    throw new ParameterResolutionException("Failed to resolve parameter of type " + parameterType + ".");
  }

  private Optional<CleanupTimeout> findCleanupTimeout(ExtensionContext context) {
    return AnnotationUtils.findAnnotation(context.getElement(), CleanupTimeout.class);
  }

  private ServerCleanupRegistry getServerCleanupRegistry(ExtensionContext context) {
    return getStore(context).getOrComputeIfAbsent(
            ServerCleanupRegistry.class,
            cls -> new ServerCleanupRegistry(
                    findCleanupTimeout(context).map(CleanupTimeout::value).orElse(DEFAULT_TIMEOUT_SECONDS),
                    findCleanupTimeout(context).map(CleanupTimeout::unit).orElse(TimeUnit.SECONDS)
            ),
            ServerCleanupRegistry.class
    );
  }

  private ManagedChannelCleanupRegistry getManagedChannelCleanupRegistry(ExtensionContext context) {
    return getStore(context).getOrComputeIfAbsent(
            ManagedChannelCleanupRegistry.class,
            cls -> new ManagedChannelCleanupRegistry(
                    findCleanupTimeout(context).map(CleanupTimeout::value).orElse(DEFAULT_TIMEOUT_SECONDS),
                    findCleanupTimeout(context).map(CleanupTimeout::unit).orElse(TimeUnit.SECONDS)
            ),
            ManagedChannelCleanupRegistry.class
    );
  }

  private ExtensionContext.Store getStore(ExtensionContext context) {
    return context.getStore(ExtensionContext.Namespace.create(GrpcTestExtension.class, context.getRequiredTestMethod()));
  }
}
