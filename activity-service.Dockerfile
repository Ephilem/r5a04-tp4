FROM maven:3.9.9-amazoncorretto-17-alpine AS build

WORKDIR /app/activity-service

# parent pom
COPY ./pom.xml ../pom.xml
# copy activity-service pom
COPY ./activity-service/pom.xml ./pom.xml
COPY activity-service/src ./src

RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/activity-service/target/sporttrack-activity-service.jar .

CMD ["java", "-jar", "sporttrack-activity-service.jar"]