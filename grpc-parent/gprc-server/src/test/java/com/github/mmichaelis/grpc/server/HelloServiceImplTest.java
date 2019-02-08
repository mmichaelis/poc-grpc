package com.github.mmichaelis.grpc.server;

import com.github.mmichaelis.grpc.service.HelloRequest;
import com.github.mmichaelis.grpc.service.HelloResponse;
import com.github.mmichaelis.grpc.service.HelloServiceGrpc;
import com.github.mmichaelis.grpc.service.Person;
import com.github.mmichaelis.grpc.test.CleanupTimeout;
import com.github.mmichaelis.grpc.test.GrpcTestExtension;
import com.github.mmichaelis.grpc.test.ManagedChannelCleanupRegistry;
import com.github.mmichaelis.grpc.test.ServerCleanupRegistry;
import com.google.protobuf.Timestamp;
import io.grpc.ManagedChannel;
import io.grpc.Server;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.lang.invoke.MethodHandles.lookup;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

@ExtendWith(GrpcTestExtension.class)
@CleanupTimeout(value = 20L, unit = TimeUnit.SECONDS)
class HelloServiceImplTest {
  private static final Logger LOG = getLogger(lookup().lookupClass());

  private HelloServiceGrpc.HelloServiceBlockingStub blockingService;
  private HelloServiceGrpc.HelloServiceStub asyncService;

  @BeforeEach
  void setUp(TestInfo info, ServerCleanupRegistry serverRegistry, ManagedChannelCleanupRegistry channelRegistry) throws IOException {
    Server server = serverRegistry.register(
            InProcessServerBuilder.forName(info.getDisplayName())
                    .addService(new HelloServiceImpl())
                    .build()
    );
    server.start();

    ManagedChannel channel = channelRegistry.register(
            InProcessChannelBuilder.forName(info.getDisplayName())
                    .usePlaintext()
                    .build()
    );

    blockingService = HelloServiceGrpc.newBlockingStub(channel);
    asyncService = HelloServiceGrpc.newStub(channel);
  }

  @Test
  void standardGreeting() {
    Person person = Person.newBuilder()
            .setFirstName("Mark")
            .setLastName("Michaelis")
            .setGender(Person.Gender.MALE)
            .setBirthday(Timestamp.newBuilder().setSeconds(LocalDate.of(1970, 1, 1).toEpochSecond(LocalTime.NOON, ZoneOffset.UTC)))
            .build();

    HelloResponse helloResponse = blockingService.hello(HelloRequest.newBuilder()
            .setPerson(person)
            .build());

    assertEquals("Hello, Mr. Mark Michaelis!", helloResponse.getGreeting());
  }

  @Test
  void birthdayGreeting() {
    Person person = Person.newBuilder()
            .setFirstName("Mark")
            .setLastName("Michaelis")
            .setGender(Person.Gender.MALE)
            .setBirthday(Timestamp.newBuilder().setSeconds(LocalDate.now().toEpochSecond(LocalTime.NOON, ZoneOffset.UTC)))
            .build();

    HelloResponse helloResponse = blockingService.hello(HelloRequest.newBuilder()
            .setPerson(person)
            .build());

    assertEquals("Happy Birthday, Mr. Mark Michaelis!", helloResponse.getGreeting());
  }

  @Test
  void streamedGreeting() throws Exception {
    CountDownLatch countDownLatch = new CountDownLatch(1);

    Stream<Person> personStream = Stream.of(
            Person.newBuilder()
                    .setFirstName("Jane")
                    .setLastName("Doe")
                    .setGender(Person.Gender.FEMALE)
                    .setBirthday(Timestamp.newBuilder().setSeconds(0))
                    .build(),
            Person.newBuilder()
                    .setFirstName("John")
                    .setLastName("Doe")
                    .setGender(Person.Gender.FEMALE)
                    .setBirthday(Timestamp.newBuilder().setSeconds(0))
                    .build()
    );

    StreamObserver<HelloRequest> requestObserver = asyncService.helloStream(new StreamObserver<>() {
      @Override
      public void onNext(HelloResponse helloResponse) {
        String greeting = helloResponse.getGreeting();
        LOG.info("Got greeting: {}", greeting);
        assertTrue(greeting.startsWith("Hello, "));
      }

      @Override
      public void onError(Throwable throwable) {
        throw new RuntimeException(throwable);
      }

      @Override
      public void onCompleted() {
        countDownLatch.countDown();
      }
    });

    try {
      personStream.forEach(p -> requestObserver.onNext(HelloRequest.newBuilder().setPerson(p).build()));
    } catch (Exception e) {
      requestObserver.onError(e);
      throw e;
    }

    requestObserver.onCompleted();
    countDownLatch.await(1, TimeUnit.MINUTES);
  }
}
