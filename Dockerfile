FROM gradle:8.3-jdk20-jammy AS builder
WORKDIR /app
COPY . .
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH=$JAVA_HOME/bin:$PATH
RUN gradle clean build --no-daemon

FROM eclipse-temurin:20-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
