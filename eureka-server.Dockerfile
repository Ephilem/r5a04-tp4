FROM maven:3.9.9-amazoncorretto-17-alpine AS build
WORKDIR /app/eureka-server
# parent pom
COPY ./pom.xml ../pom.xml
# copy eureka-server pom
COPY ./eureka-server/pom.xml ./pom.xml
COPY eureka-server/src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/eureka-server/target/sporttrack-eureka-server.jar .
CMD ["java", "-jar", "sporttrack-eureka-server.jar"]