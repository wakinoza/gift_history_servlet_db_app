
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk21-temurin
WORKDIR /usr/local/tomcat

COPY --from=builder /app/target/*.war webapps/gift.war

ENV CATALINA_OPTS="-Dorg.apache.tomcat.util.digester.PROPERTY_SOURCE=org.apache.tomcat.util.digester.EnvironmentPropertySource"

COPY context.xml conf/context.xml

EXPOSE 8080
CMD ["catalina.sh", "run"]