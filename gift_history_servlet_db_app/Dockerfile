FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:10.1-jdk21-temurin
WORKDIR /usr/local/tomcat

COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war
COPY context.xml /usr/local/tomcat/conf/context.xml


RUN chown -R 1001:1001 /usr/local/tomcat/webapps /usr/local/tomcat/logs /usr/local/tomcat/work /usr/local/tomcat/temp /usr/local/tomcat/conf
USER 1001

ENV CATALINA_OPTS="-Dorg.apache.tomcat.util.digester.PROPERTY_SOURCE=org.apache.tomcat.util.digester.EnvironmentPropertySource -Djava.io.tmpdir=/usr/local/tomcat/temp"
EXPOSE 8080
CMD ["catalina.sh", "run"]