# StockBoard - 주식 커뮤니티 플랫폼

Spring Boot 기반의 주식 커뮤니티 웹 애플리케이션입니다.

## 🚀 주요 기능

- **실시간 주식 정보**: 최신 주식 가격 및 변동 정보 제공
- **커뮤니티 게시판**: 투자자들의 소통 공간
- **주식 챗봇**: AI 기반 주식 정보 질의응답
- **사용자 관리**: 회원가입, 로그인, 로그아웃 기능
- **반응형 디자인**: 모바일과 데스크톱 모두 지원

## 🛠 기술 스택

- **Backend**: Spring Boot 3.5.3, Java 21
- **Database**: MySQL 8.0
- **ORM**: MyBatis
- **Frontend**: Thymeleaf, HTML5, CSS3, JavaScript
- **UI Framework**: Font Awesome (아이콘)
- **Build Tool**: Maven

## 📁 프로젝트 구조

```
StockBoard/
├── src/
│   ├── main/
│   │   ├── java/com/example/stockboard/
│   │   │   ├── controller/          # 컨트롤러
│   │   │   ├── domain/              # 도메인 모델
│   │   │   ├── mapper/              # MyBatis 매퍼
│   │   │   ├── service/             # 서비스 인터페이스
│   │   │   │   └── impl/            # 서비스 구현체
│   │   │   └── StockBoardApplication.java
│   │   └── resources/
│   │       ├── static/              # 정적 리소스
│   │       │   ├── css/             # 스타일시트
│   │       │   └── js/              # 자바스크립트
│   │       ├── templates/           # Thymeleaf 템플릿
│   │       ├── mybatis/             # MyBatis XML 매퍼
│   │       ├── application.properties
│   │       └── schema.sql           # 데이터베이스 스키마
│   └── test/                        # 테스트 코드
├── pom.xml
└── README.md
```

## 🚀 실행 방법

### 1. 사전 요구사항

- Java 21 이상
- MySQL 8.0 이상
- Maven 3.6 이상

### 2. 데이터베이스 설정

1. MySQL 서버를 시작합니다.
2. `kopodb` 데이터베이스를 생성합니다:
   ```sql
   CREATE DATABASE kopodb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```
3. 사용자를 생성하고 권한을 부여합니다:
   ```sql
   CREATE USER 'kopouser'@'localhost' IDENTIFIED BY 'kopouser';
   GRANT ALL PRIVILEGES ON kopodb.* TO 'kopouser'@'localhost';
   FLUSH PRIVILEGES;
   ```
4. `src/main/resources/schema.sql` 파일을 실행하여 테이블과 샘플 데이터를 생성합니다.

### 3. 애플리케이션 실행

1. 프로젝트 루트 디렉토리에서 다음 명령을 실행합니다:
   ```bash
   mvn spring-boot:run
   ```

2. 웹 브라우저에서 `http://localhost:8080`으로 접속합니다.

## 📱 주요 페이지

- **홈페이지** (`/`): 실시간 주식 정보, 최신 게시글, 챗봇
- **로그인** (`/login`): 사용자 로그인
- **회원가입** (`/join`): 새 사용자 등록
- **게시판** (`/board`): 커뮤니티 게시판
- **주식정보** (`/stocks`): 상세 주식 정보

## 🎨 UI/UX 특징

- **모던한 디자인**: 그라데이션과 그림자 효과
- **반응형 레이아웃**: 모바일, 태블릿, 데스크톱 지원
- **인터랙티브 요소**: 호버 효과, 애니메이션
- **사용자 친화적**: 직관적인 네비게이션과 알림 시스템

## 🔧 개발 환경 설정

### IDE 설정 (IntelliJ IDEA)

1. 프로젝트를 열고 Maven 프로젝트로 인식되도록 설정
2. Java 21 SDK 설정
3. Spring Boot 플러그인 활성화

### 디버깅

- 애플리케이션 로그는 `DEBUG` 레벨로 설정되어 있습니다.
- MyBatis 쿼리 로그도 확인할 수 있습니다.

## 📊 데이터베이스 스키마

### 주요 테이블

- **users**: 사용자 정보
- **stocks**: 주식 정보
- **boards**: 게시글

### 샘플 데이터

- 10개의 주요 주식 정보
- 5개의 샘플 게시글
- 3개의 테스트 사용자 계정

## 🚀 배포

### JAR 파일 빌드

```bash
mvn clean package
```

### 실행

```bash
java -jar target/StockBoard-0.0.1-SNAPSHOT.jar
```

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.

## 📞 문의

프로젝트에 대한 문의사항이 있으시면 이슈를 생성해 주세요.

---

**StockBoard** - 주식 커뮤니티의 새로운 시작 🚀 