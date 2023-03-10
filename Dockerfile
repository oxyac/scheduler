FROM gradle:jdk17-focal as build

WORKDIR /build

RUN mkdir -p /root/.ssh \
    && chmod 0700 /root/.ssh \
    && ssh-keyscan -t rsa github.com >> /root/.ssh/known_hosts \
    && git clone https://github.com/oxyac/scheduler.git . \
    && gradle build

FROM bellsoft/liberica-openjre-debian:17
COPY --from=build /build/build/libs/app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
