FROM maven:3.9.9-amazoncorretto-17-alpine AS build

WORKDIR /app/user-service

# parent pom
COPY ./pom.xml ../pom.xml
# copy user-service pom
COPY ./user-service/pom.xml ./pom.xml
COPY user-service/src ./src

RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/user-service/target/sporttrack-user-service.jar .

CMD ["java", "-jar", "sporttrack-user-service.jar"]