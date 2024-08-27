# Builder stage
FROM openjdk:22-slim AS builder
WORKDIR book-store-application
ARG JAVA_FILE=target/*.jar
COPY ${JAVA_FILE} book-store-app.jar
RUN java -Djarmode=layertools -jar book-store-app.jar extract

# Final stage
FROM openjdk:22-slim
WORKDIR book-store-application
COPY --from=builder book-store-application/dependencies/ ./
COPY --from=builder book-store-application/spring-boot-loader/ ./
COPY --from=builder book-store-application/snapshot-dependencies/ ./
COPY --from=builder book-store-application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8080