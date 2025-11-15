FROM gradle:8.3.1-jdk20 AS builder
WORKDIR /app
COPY . .
RUN gradle clean build --no-daemon

FROM eclipse-temurin:20-jdk
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
