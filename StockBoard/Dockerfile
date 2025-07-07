FROM openjdk:21-slim

WORKDIR /app

# 빌드된 JAR 파일을 이미지 안으로 복사
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar", "--spring.profiles.active=prod"]


