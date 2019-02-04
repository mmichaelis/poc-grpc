module grpc.service {
  requires com.google.common;
  requires java.annotation;
  requires java.compiler;
  requires grpc.core;
  requires grpc.protobuf;
  requires grpc.stub;
  requires protobuf.java;

  exports com.github.mmichaelis.grpc.service;
}
