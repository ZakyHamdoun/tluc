FROM openjdk:24-jdk-slim

WORKDIR /app

COPY pom.xml .
COPY tluc-app/pom.xml ./tluc-app/
COPY tluc-common/pom.xml ./tluc-common/
COPY tluc-dao/pom.xml ./tluc-dao/
COPY tluc-domain/pom.xml ./tluc-domain/
COPY tluc-dto/pom.xml ./tluc-dto/
COPY tluc-service/pom.xml ./tluc-service/

COPY tluc-app/src ./tluc-app/src
COPY tluc-common/src ./tluc-common/src
COPY tluc-dao/src ./tluc-dao/src
COPY tluc-domain/src ./tluc-domain/src
COPY tluc-dto/src ./tluc-dto/src
COPY tluc-service/src ./tluc-service/src

RUN apt-get update && apt-get install -y maven

RUN mvn clean package -DskipTests

RUN mkdir -p /app/logs

EXPOSE 8080

# The command to run the application
CMD ["java", "-jar", "tluc-app/target/tluc-app-0.0.1-SNAPSHOT.jar"] 