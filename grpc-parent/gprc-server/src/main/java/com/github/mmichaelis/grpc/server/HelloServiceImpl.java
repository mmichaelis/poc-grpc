package com.github.mmichaelis.grpc.server;

import com.github.mmichaelis.grpc.service.HelloRequest;
import com.github.mmichaelis.grpc.service.HelloResponse;
import com.github.mmichaelis.grpc.service.HelloServiceGrpc;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.grpc.stub.StreamObserver;

@DefaultAnnotation(NonNull.class)
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
  @Override
  public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
    String greeting = new StringBuilder()
            .append("Hello, ")
            .append(request.getPerson().getFirstName())
            .append(" ")
            .append(request.getPerson().getLastName())
            .toString();

    HelloResponse response = HelloResponse.newBuilder()
            .setGreeting(greeting)
            .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
