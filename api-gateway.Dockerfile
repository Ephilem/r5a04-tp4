FROM maven:3.9.9-amazoncorretto-17-alpine AS build

WORKDIR /app/api-gateway

# parent pom
COPY ./pom.xml ../pom.xml
# copy api-gateway pom
COPY ./api-gateway/pom.xml ./pom.xml
COPY api-gateway/src ./src

RUN mvn clean package -DskipTests


FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/api-gateway/target/sporttrack-api-gateway.jar .

CMD ["java", "-jar", "sporttrack-api-gateway.jar"]