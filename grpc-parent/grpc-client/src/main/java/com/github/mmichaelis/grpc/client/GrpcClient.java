package com.github.mmichaelis.grpc.client;

import com.github.mmichaelis.grpc.service.HelloRequest;
import com.github.mmichaelis.grpc.service.HelloResponse;
import com.github.mmichaelis.grpc.service.HelloServiceGrpc;
import com.github.mmichaelis.grpc.service.Person;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GrpcClient {
  public static void main(String[] args) {
    ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
            .usePlaintext()
            .build();

    HelloServiceGrpc.HelloServiceBlockingStub stub
            = HelloServiceGrpc.newBlockingStub(channel);

    Person person = Person.newBuilder()
            .setFirstName("Mark")
            .setLastName("Michaelis")
            .setGender(Person.Gender.MALE)
            .build();

    HelloResponse helloResponse = stub.hello(HelloRequest.newBuilder()
            .setPerson(person)
            .build());

    System.out.println(helloResponse.getGreeting());

    channel.shutdown();
  }
}
