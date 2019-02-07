package com.github.mmichaelis.grpc.test;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

/**
 * Exception on Cleanup.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
@DefaultAnnotation(NonNull.class)
public class CleanupException extends RuntimeException {
  public CleanupException() {
    super();
  }

  public CleanupException(String message) {
    super(message);
  }

  public CleanupException(String message, Throwable cause) {
    super(message, cause);
  }

  public CleanupException(Throwable cause) {
    super(cause);
  }

  protected CleanupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
