# 빌드 스테이지
FROM gradle:8.5-jdk21 AS builder
WORKDIR /app

# Gradle 파일 복사 (캐싱 최적화)
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

RUN gradle dependencies --no-daemon || true

COPY src ./src

# 애플리케이션 빌드
RUN gradle clean build -x test --no-daemon

# 실행 스테이지
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

# 환경 변수 기본값 prod 프로필 사용
ENV SPRING_PROFILES_ACTIVE=prod

# 노출 포트 선언
EXPOSE 8080

# 컨테이너 시작
ENTRYPOINT ["java", "-jar", "/app/app.jar"]