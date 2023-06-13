#
# Build stage
#
#FROM maven:3.8.2-jdk-11 AS build
#COPY . .
#RUN mvn clean package -DskipTests

#
# Package stage
#
#FROM openjdk:17
##COPY --from=build /target/demoEcommerce-0.0.1-SNAPSHOT.jar app.jar
#COPY target/demoEcommerce-0.0.1-SNAPSHOT.jar app.jar
## ENV PORT=8080
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM maven:3.9.0-eclipse-temurin-17-alpine AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -Dmaven.test.skip

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /home/app/target/demoEcommerce-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/app.jar"]