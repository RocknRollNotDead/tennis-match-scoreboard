
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /project
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

#FROM eclipse-temurin:21-jre
#WORKDIR /project
#COPY --from=build /project/target/*.war app.war
#ENTRYPOINT ["java", "-jar", "app.war"]

FROM tomcat:10-jdk21
COPY --from=build /project/target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]