FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/employee-management.jar app.jar
ENV PORT=8080
EXPOSE 8080
VOLUME ["/app/data"]
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
