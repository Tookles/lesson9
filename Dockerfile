FROM maven:3-eclipse-temurin-17 AS build

WORKDIR /opt
COPY pom.xml .
RUN mvn dependency:resolve
COPY src ./src
RUN mvn clean package -D maven.test.skip

FROM eclipse-temurin:17-jre AS run

WORKDIR /opt
COPY --from=build /opt/target/lesson9-0.0.1-SNAPSHOT.jar .

ENTRYPOINT [ "java", "-jar", "lesson9-0.0.1-SNAPSHOT.jar" ]
EXPOSE 8080
