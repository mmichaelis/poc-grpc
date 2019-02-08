package com.github.mmichaelis.grpc.server;

import com.github.mmichaelis.grpc.service.HelloRequest;
import com.github.mmichaelis.grpc.service.HelloResponse;
import com.github.mmichaelis.grpc.service.HelloServiceGrpc;
import com.github.mmichaelis.grpc.service.Person;
import edu.umd.cs.findbugs.annotations.DefaultAnnotation;
import edu.umd.cs.findbugs.annotations.NonNull;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@DefaultAnnotation(NonNull.class)
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
  @Override
  public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
    StringBuilder builder = new StringBuilder();
    Person person = request.getPerson();

    long birthdayEpochSecondsUtc = person.getBirthday().getSeconds();
    LocalDateTime birthdayDateTime = LocalDateTime.ofEpochSecond(birthdayEpochSecondsUtc, 0, ZoneOffset.UTC);
    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    if (ChronoUnit.DAYS.between(birthdayDateTime, now) == 0L) {
      builder.append("Happy Birthday, ");
    } else {
      builder.append("Hello, ");
    }

    switch (person.getGender()) {
      case MALE:
        builder.append("Mr. ");
        break;
      case FEMALE:
        builder.append("Ms. ");
        break;
      case OTHER:
        break;
      case UNRECOGNIZED:
        builder.append("Mr. or Ms. ");
      default:
        // nothing to do
    }
    builder.append(person.getFirstName())
            .append(" ")
            .append(person.getLastName())
            .append("!");

    HelloResponse response = HelloResponse.newBuilder()
            .setGreeting(builder.toString())
            .build();

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
