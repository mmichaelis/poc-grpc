package com.github.mmichaelis.grpc.server;

import com.github.mmichaelis.grpc.service.HelloRequest;
import com.github.mmichaelis.grpc.service.HelloResponse;
import com.github.mmichaelis.grpc.service.HelloServiceGrpc;
import com.github.mmichaelis.grpc.service.Person;
import com.github.mmichaelis.grpc.test.CleanupTimeout;
import com.github.mmichaelis.grpc.test.GrpcTestExtension;
import com.github.mmichaelis.grpc.test.ManagedChannelCleanupRegistry;
import com.github.mmichaelis.grpc.test.ServerCleanupRegistry;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GrpcTestExtension.class)
@CleanupTimeout(value = 20L, unit = TimeUnit.SECONDS)
class HelloServiceImplTest {

  private ManagedChannel channel;

  @BeforeEach
  void setUp(ServerCleanupRegistry serverRegistry, ManagedChannelCleanupRegistry channelRegistry) throws IOException {
    Server server = serverRegistry.register(ServerBuilder.forPort(0)
            .addService(new HelloServiceImpl()).build()
    );
    server.start();

    int serverPort = server.getPort();

    channel = channelRegistry.register(ManagedChannelBuilder.forAddress("localhost", serverPort)
            .usePlaintext()
            .build());
  }

  @Test
  void serverIsStarted() {
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

    assertEquals("Hello, Mr. Mark Michaelis", helloResponse.getGreeting());
  }
}
