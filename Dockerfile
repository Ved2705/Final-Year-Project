# -------------------------------------------------------------
# Stage 1: Build React Frontend
# -------------------------------------------------------------
FROM node:20-alpine AS frontend-build
WORKDIR /frontend
COPY frontend-react/package.json frontend-react/package-lock.json* ./
RUN npm install
COPY frontend-react/ ./
ENV VITE_API_URL=/api
RUN npm run build

# -------------------------------------------------------------
# Stage 2: Build Spring Boot Backend (embedding React assets)
# -------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17 AS backend-build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
# Copy built React files into Spring Boot's static folder
COPY --from=frontend-build /frontend/dist/ ./src/main/resources/static/
RUN mvn package -DskipTests

# -------------------------------------------------------------
# Stage 3: Minimal Production Alpine JRE Runtime
# -------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=backend-build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
