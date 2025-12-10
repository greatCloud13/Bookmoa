#  도서 리뷰 플랫폼

> Redis 캐싱과 RabbitMQ 비동기 처리를 활용한 고성능 도서 검색/리뷰 서비스

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Redis](https://img.shields.io/badge/Redis-7-red.svg)](https://redis.io/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3-orange.svg)](https://www.rabbitmq.com/)

##  목차

- [프로젝트 소개](#-프로젝트-소개)
- [핵심 성과](#-핵심-성과)
- [기술 스택](#-기술-스택)
- [아키텍처](#-아키텍처)
- [주요 기능](#-주요-기능)
- [성능 개선](#-성능-개선)
- [실행 방법](#-실행-방법)
- [API 문서](#-api-문서)

---

##  프로젝트 소개

**Redis 캐싱**과 **RabbitMQ 비동기 처리**를 중심으로, 외부 API 호출 최소화 및 응답 속도 개선에 집중했습니다.

### 개발 배경
- 알라딘 도서 API는 일일 호출 제한(500회)이 있음
- 검색의 경우 동일한 요청이 반복적으로 발생
- 리뷰 작성 시 통계 계산으로 인한 응답 지연

### 해결 방안
- **Redis 캐싱**: 검색 결과 및 도서 상세 정보 캐싱
- **RabbitMQ**: 통계 계산을 비동기로 처리
- **JMeter**: 성능 개선 효과를 정량적으로 측정

---

##  핵심 성과

### 1. 도서 검색 API 성능 개선

| 구분 | 응답 시간 | 개선 효과 |
|------|----------|-----------|
| 1차 요청 (캐시 없음) | 234ms | - |
| 2차 이후 (Redis 캐시) | 20ms | **91.5% 감소** |

 **서버 응답 속도 11.7배 향상**  
 알라딘 외부 API 호출 → Redis 메모리 조회로 전환

### 2. 도서 상세 조회 API 성능 개선

| 구분 | 응답 시간 | 개선 효과 |
|------|----------|-----------|
| 1차 요청 (DB 조회) | 87ms | - |
| 2차 이후 (Redis 캐시) | 8ms | **90.8% 감소** |

 **서버 응답 속도 10.8배 향상**  
 DB I/O → Redis 메모리 조회로 전환

 Docker Compose + Git Actions 기반 **CI/CD 자동 배포** 구현

---

##  기술 스택

### Backend
- **Framework**: Spring Boot 3.4.5
- **Language**: Java 21
- **ORM**: Spring Data JPA, Hibernate
- **Cache**: Redis 7, Spring Cache
- **Message Queue**: RabbitMQ 3

### Database
- **RDBMS**: MySQL 8.0

### Infrastructure
- **Containerization**: Docker, Docker Compose
- **API Test**: Swagger, Postman
- **Performance Test**: Apache JMeter

### External API
- **알라딘 도서 검색 API**

---

##  아키텍처

### 시스템 아키텍처
```
            [User]
              ↓
       [Spring Boot API]
        ↓       ↓       ↓
[Redis Cache] [MySQL] [RabbitMQ]
  ↓
[Aladin API]
```

### 캐싱 전략
- **도서 검색**: TTL 30분 (신간 대응)
- **도서 상세**: TTL 6시간 (정보 변경 드뭄)
- **인기 도서**: TTL 1시간 (실시간성 중시)

### 비동기 처리 흐름
```
리뷰 작성
  ↓
이벤트 발행 (RabbitMQ)
  ↓
Consumer가 통계 계산
  ↓
DB 업데이트 + 캐시 무효화
```

---

##  주요 기능

### 1. 도서 검색
- 알라딘 Open API 연동
- 검색 결과 Redis 캐싱 (30분)
- 페이징 지원

### 2. 도서 상세 조회
- ISBN 기반 조회
- 첫 조회: 알라딘 API → DB 저장
- 이후 조회: Redis 캐시
- 표지, 저자, 출판사, 출판일 정보

### 3. 리뷰 시스템
- 리뷰 CRUD (작성, 조회, 수정, 삭제)
- 평점 (1-5점)
- 리뷰 작성 시 통계 자동 계산 (비동기)

### 4. 인기 도서 랭킹
- 베이지안 평균 알고리즘 적용
- 리뷰 수와 평점을 모두 고려
- 신뢰도 높은 랭킹 제공

### 5. 도서 통계
- 평균 평점 자동 계산
- 리뷰 수 집계
- RabbitMQ로 비동기 처리

---

##  성능 개선

### 테스트 환경
- **Tool**: Apache JMeter
- **동시 사용자**: 100명
- **Ramp-up**: 10초
- **측정 지표**: Latency (서버 응답 시간)

### 개선 결과 요약
- **도서 검색**: 234ms → 20ms (**91.5% 감소**)
- **도서 상세**: 87ms → 8ms (**90.8% 감소**)
- **외부 API 호출**: 90% 이상 감소

---

##  실행 방법

### 1. 사전 준비
- Docker & Docker Compose 설치
- Java 21 설치
- 알라딘 API 키 발급

### 2. 환경 변수 설정
```bash
# .env 파일 생성
cp .env.example .env

# 알라딘 API 키 입력
ALADIN_API_KEY=your_api_key_here
```

### 3. Docker 실행
```bash
# MySQL, Redis, RabbitMQ 실행
docker-compose up -d
```

### 4. 애플리케이션 실행
```bash
# Gradle로 실행
./gradlew bootRun

# 또는 JAR 빌드 후 실행
./gradlew build
java -jar build/libs/bookmoa-0.0.1-SNAPSHOT.jar
```

### 5. 접속 확인
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **RabbitMQ Management**: http://localhost:15672 (bookmoa/bookmoa1234)

---

##  API 문서

Swagger UI를 통해 모든 API를 확인하고 테스트할 수 있습니다.

### 주요 엔드포인트

| Method | Endpoint | 설명 |
|--------|----------|------|
| GET | `/api/books/search` | 도서 검색 |
| GET | `/api/books/{isbn}` | 도서 상세 조회 |
| GET | `/api/books/popular` | 인기 도서 조회 |
| POST | `/api/reviews` | 리뷰 작성 |
| GET | `/api/reviews?isbn={isbn}` | 특정 책의 리뷰 목록 |
| PUT | `/api/reviews/{id}` | 리뷰 수정 |
| DELETE | `/api/reviews/{id}` | 리뷰 삭제 |

자세한 API 명세는 Swagger 문서를 참고하세요.

---

##  기술적 의사결정

### 1. Redis 캐시 TTL 차별화
- 검색 결과 (30분): 신간 도서 반영
- 도서 상세 (6시간): 정보 변경 드뭄
- 인기 도서 (1시간): 실시간성 중시

### 2. RabbitMQ 이벤트 설계
- ISBN만 메시지로 전달 (경량 메시지)
- Consumer가 DB 조회 후 계산 (최신 데이터 보장)
- 멱등성 확보 (중복 처리 시에도 정확)

### 3. 베이지안 평균 알고리즘
- 리뷰 수와 평점을 모두 고려
- 신뢰도 낮은 데이터 보정 (최소 리뷰 수 5개 기준)

---

##  참고 자료

- [Spring Cache Documentation](https://docs.spring.io/spring-framework/reference/integration/cache.html)
- [RabbitMQ Tutorials](https://www.rabbitmq.com/getstarted.html)
- [알라딘 Open API](https://blog.aladin.co.kr/openapi)

##  향후 계획

- [ ] AWS EC2 배포
- [ ] GitHub Actions CI/CD 구축
- [ ] 예외 처리 강화 (GlobalExceptionHandler)
- [ ] 통합 테스트 작성
- [ ] Prometheus + Grafana 모니터링
