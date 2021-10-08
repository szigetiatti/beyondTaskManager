#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /src
COPY pom.xml /
RUN mvn -f /pom.xml clean package &&\
mvn -f /pom.xml liquibase:diff -Pprod -Dliquibase.url=jdbc:h2:/src/main/resources/db/testProdDB -Dliquibase.referenceUrl=jdbc:h2:/src/main/resources/db/testLocalDB &&\
mvn -f /pom.xml liquibase:update -Pprod -Dliquibase.url=jdbc:h2:/src/main/resources/db/testProdDB

#
# Package stage
#
FROM openjdk:11.0.10-jdk-oracle
COPY --from=build /target/demo-0.0.1-SNAPSHOT.jar app.jar
COPY --from=build /src/main/resources/db/testProdDB.mv.db /src/main/resources/db/testProdDB.mv.db
COPY --from=build /src/main/resources/db/testProdDB.trace.db /src/main/resources/db/testProdDB.trace.db
ENV JAVA_OPTS="-Dspring.profiles.active=prod"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar"]