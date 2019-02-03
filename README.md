# gRPC Proof-of-Concept

This is a proof-of-concept for using gRPC. In addition to that, the project
is based on Java 11 and uses its module system.

## IDE Support

For IntelliJ Idea it is recommended to install:

* [Protobuf Support][jetbrains.plugins.protobuf]

in order to get support editing `.proto` files.

## Notes

* Snake-case field names will be transformed to camel-case.

## Troubleshooting

### `javax.annotation` does not exist

Caused by grpc/grpc-java#3633, the annotation to mark generated classes
`javax.annotation.Generated` is not available. Some projects mentioned
a similar problem &mdash; the overall problem is to support Java 8- and
Java 9+ in parallel.

While you would use `javax.annotation.processing.Generated` in Java 9+
projects, it is `javax.annotation.Generated` for Java 8-. Current workaround
for Java 11 is to add the following dependency (for example as provided scope):

```xml
<dependency>
  <groupId>javax.annotation</groupId>
  <artifactId>javax.annotation-api</artifactId>
  <version>1.3.1</version>
</dependency>
```

### See Also

* [grpc.io][]
* [grpc@GitHub][github.grpc]
* [Introduction to gRPC | Baeldung][baeldung.grpc]
* [Language Guide (proto3) | Protocol Buffers | Google Developers][googledev.proto3]
* [Maven Protocol Buffers Plugin – Introduction][maven.plugin.protobuf]
* [Protobuf Support - Plugins | JetBrains][jetbrains.plugins.protobuf]
* [Protocol Buffers | Google Developers][googledev.protobuf]

[baeldung.grpc]: <https://www.baeldung.com/grpc-introduction> "Introduction to gRPC | Baeldung"
[github.grpc]: <https://github.com/grpc/> "grpc"
[googledev.proto3]: <https://developers.google.com/protocol-buffers/docs/proto3> "Language Guide (proto3)  |  Protocol Buffers  |  Google Developers"
[googledev.protobuf]: <https://developers.google.com/protocol-buffers/> "Protocol Buffers  |  Google Developers"
[grpc.io]: <https://grpc.io/> "grpc / grpc.io"
[jetbrains.plugins.protobuf]: <https://plugins.jetbrains.com/plugin/8277-protobuf-support> "Protobuf Support - Plugins | JetBrains"
[maven.plugin.protobuf]: <https://www.xolstice.org/protobuf-maven-plugin/> "Maven Protocol Buffers Plugin – Introduction"
