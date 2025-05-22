FROM maven:3.9.9-eclipse-temurin-24 AS build
WORKDIR /app
COPY . .
RUN mvn clean install -Dmaven.test.skip=true

FROM eclipse-temurin:24-alpine-3.21
WORKDIR /app
COPY --from=build /app/target/user-microservice-0.0.1-SNAPSHOT.jar /app/
EXPOSE 8999
ENTRYPOINT ["java", "-jar", "/app/user-microservice-0.0.1-SNAPSHOT.jar"]
