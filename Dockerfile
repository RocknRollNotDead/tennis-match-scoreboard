
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /project
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:10-jdk21
COPY --from=build /project/target/*.war /usr/local/tomcat/webapps/tennis-match-scoreboard.war
EXPOSE 8080
CMD ["catalina.sh", "run"]