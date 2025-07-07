# 📊 StockBotBoard

**StockBotBoard**는 주가 데이터, 기술적 분석, 뉴스 요약을 기반으로 종목 리포트를 자동 생성해주는 통합 시스템입니다.  
Spring Boot 기반 웹 백엔드와 FastAPI 기반 LLM 리포트 생성기가 Docker로 통합되어 있습니다.

---

## 🛠️ 기술 스택

- **Spring Boot**: 사용자/게시판 관리 및 프론트 역할
- **FastAPI + LangChain**: 종목 리포트 생성기 (LLM 기반)
- **MySQL**: 데이터베이스 (회원, 게시글, 검색 로그 저장)
- **Ollama**: 로컬 LLM (예: `gemma:7b`)
- **Docker / docker-compose**: 전체 서비스 통합 실행
- **FAISS**: 뉴스 벡터 검색용

---

## 📁 프로젝트 구조

```
StockBotBoard/
├── StockBoard/        # Spring Boot 백엔드 (포트 8080)
├── StockBot/          # FastAPI 기반 리포트 생성기 (포트 8000)
├── docker-compose.yml # 모든 서비스 통합 실행
├── .env.example       # 환경 변수 템플릿
└── README.md          # 프로젝트 설명 문서
```

---

## ⚙️ 설치 및 실행 방법

```bash
# 1. 프로젝트 클론
git clone https://github.com/leeeseungju/StockBotBoard.git
cd StockBotBoard

# 2. 환경 변수 설정
cp .env.example .env

# 3. Docker 실행
docker compose up -d
```

→ 브라우저에서 `http://localhost:8080` 접속  
→ 종목 요약 요청: `/stock` 또는 FastAPI `/report` API 사용

---

## 🔐 환경 변수 설정 (`.env.example` 참고)

```env
# FastAPI / StockBot
OLLAMA_HOST=http://ollama:11434
MODEL_NAME=gemma:7b
REPORT_OUTPUT_PATH=/charts

# MySQL
DB_HOST=mysql
DB_PORT=3306
DB_NAME=kopodb
DB_USER=root
DB_PASSWORD=password

# 기타
NEWS_API_KEY=your-news-api-key-here
```

---

## 📮 API 명세

### [POST] `/report` (FastAPI)

> 종목 리포트 생성 요청

#### ✅ 요청 예시:

```json
{
  "message": "삼성전자 요약해줘"
}
```

#### ✅ 응답 예시:

```json
{
  "summary": "삼성전자는 최근...",
  "summary_chart_path": "/charts/summary_chart.png",
  "tech_chart_path": "/charts/tech_chart.png"
}
```

→ 결과는 `charts/` 디렉토리에 이미지로 저장됨

---

## 🖼️ 결과 예시

| 차트 | 설명 |
|------|------|
| 📈 summary_chart.png | 종목의 최근 주가 요약 그래프 |
| 📊 tech_chart.png | 기술적 분석 (RSI, MACD 등) 시각화 |

---

## 🧙 관리자 기능

- 게시판 CRUD
- 댓글 시스템
- 회원 목록 조회
- 검색 로그 확인
- 마이페이지(내 정보 수정)

---

## 🐳 Docker 서비스 요약

| 서비스 | 설명 | 포트 |
|--------|------|------|
| stockboard | Spring Boot 백엔드 | 8080 |
| stockbot   | FastAPI + LLM | 8000 |
| mysql      | MySQL DB | 3306 |
| ollama     | gemma:7b 모델 서빙 | 11434 |

---

## 📌 주의사항

- `.env`는 Git에 올라가지 않도록 `.gitignore` 되어 있어야 합니다
- `.env.example`을 기준으로 환경 설정을 복사해서 사용하세요
- Docker 사용이 어려운 경우, 각 서비스를 수동으로 실행할 수도 있습니다

---

## 📬 문의 / 기여

> 본 프로젝트는 개인 학습 및 연구 목적으로 제작되었습니다.  
기여를 원하시면 Pull Request를 보내주세요 🙌
