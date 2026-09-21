# ---------------------------------------------------------------------------
# Build stage: compiles the app and pulls dto/entity/mapper from GitHub Packages
# ---------------------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# GitHub Packages credentials.
ARG MAVEN_USERNAME
ARG MAVEN_TOKEN
ENV MAVEN_USERNAME=${MAVEN_USERNAME}
ENV MAVEN_TOKEN=${MAVEN_TOKEN}

COPY ci/settings.xml ./settings.xml

# Dependency layer
COPY pom.xml ./
RUN mvn -B -ntp -s settings.xml dependency:go-offline || true

# Build
COPY src ./src
RUN mvn -B -ntp -s settings.xml clean package -DskipTests

# ---------------------------------------------------------------------------
# Runtime stage: slim JRE, non-root, honours Render's $PORT
# ---------------------------------------------------------------------------
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app

RUN groupadd --system app && useradd --system --gid app app
COPY --from=build /build/target/elemental-monster-duel-api-*.jar app.jar
USER app

# Tuned for Render
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseSerialGC -Xss512k"
EXPOSE 8080

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar --server.port=${PORT:-8080}"]
