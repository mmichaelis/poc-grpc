package com.github.mmichaelis.grpc.server;

import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

@DefaultAnnotation(NonNull.class)
public class GrpcServer {
  public static void main(String[] args) throws IOException, InterruptedException {
    Server server = ServerBuilder
            .forPort(8080)
            .addService(new HelloServiceImpl()).build();

    server.start();
    server.awaitTermination();
  }
}
