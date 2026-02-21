# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copy only Gradle files first (maximizes cache)
COPY gradlew .
RUN chmod +x gradlew

COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Cache Gradle downloads
RUN --mount=type=cache,target=/root/.gradle,id=gradle-cache-userservice \
    ./gradlew dependencies --no-daemon

# Now copy source
COPY src src

# Build the jar (reuse cache)
RUN --mount=type=cache,target=/root/.gradle,id=gradle-cache-userservice \
    ./gradlew bootJar --no-daemon

# Stage 2: Runtime
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar user-service.jar
ENTRYPOINT ["java","-Duser.timezone=Asia/Kolkata", "-jar", "user-service.jar"]