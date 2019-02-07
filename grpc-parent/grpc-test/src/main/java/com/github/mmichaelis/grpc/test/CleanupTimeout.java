package com.github.mmichaelis.grpc.test;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Grace period for cleanup each single service.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@DefaultAnnotation(NonNull.class)
public @interface CleanupTimeout {
  /**
   * Amount of time units.
   *
   * @return time units
   */
  long value();

  /**
   * Time unit for timeout.
   *
   * @return time unit
   */
  TimeUnit unit();
}
