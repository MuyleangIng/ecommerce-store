FROM gradle:8.4-jdk17-alpine As builder
ENV SPRING_PROFILES_ACTIVE=prod
WORKDIR /app
COPY . .
# Build the application
RUN gradle build --no-daemon
# Move the jar file from the build/libs directory to the Docker image
RUN mv ./build/libs/*-SNAPSHOT.jar ecommerce.jar
VOLUME /images
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=\${SPRING_PROFILES_ACTIVE}", "ecommerce.jar"]