import os
import re
import requests
import pandas as pd
import yfinance as yf
from config import ALPHA_VANTAGE_API_KEY, GOOGLE_API_KEY, SEARCH_ENGINE_ID
from langchain_community.llms import Ollama

# 종목 코드 CSV 경로
STOCK_CODE_PATH = "/app/data/stock_codes.csv"

# 종목코드 자동 저장 함수
def append_to_stock_codes(name, code):
    import csv

    # 중복 확인
    if os.path.exists(STOCK_CODE_PATH):
        with open(STOCK_CODE_PATH, 'r', encoding='utf-8') as file:
            lines = file.readlines()
            for line in lines:
                if name in line or code in line:
                    return  # 이미 존재하면 저장 안 함

    # 항상 줄바꿈 보장하며 추가
    with open(STOCK_CODE_PATH, 'a', encoding='utf-8', newline='') as file:
        writer = csv.writer(file)
        writer.writerow([name.strip(), code.strip()])


# 종목명으로 종목코드 반환
def get_code_by_name(name):
    try:
        df = pd.read_csv(STOCK_CODE_PATH)
        match = df[df['name'].str.contains(name, na=False)]
        if not match.empty:
            return match.iloc[0]['code']
    except Exception as e:
        print(f"[get_code_by_name 오류] {e}")

    # fallback: LLM 추론
    try:
        llm = Ollama(model="gemma:7b")
        response = llm.invoke(
            f'"{name}"의 한국 주식 종목명을 분석해서 Yahoo Finance 종목코드(KRX 또는 KQ 형식 포함)로 알려줘. 예: 삼성전자 → 005930.KS'
        )

        # 우선 → 기준 추출
        if "→" in response:
            code = response.split("→")[1].strip()
        else:
            # 코드 패턴 직접 탐색
            match = re.search(r'\b\d{6}\.[A-Z]{2}\b', response)
            if match:
                code = match.group(0)
            else:
                print(f"[LLM 응답 확인 필요] '{response}'")
                return None

        # CSV 자동 저장
        append_to_stock_codes(name, code)
        return code

    except Exception as e:
        print(f"[LLM fallback 오류] {e}")
        return None


# 주가 데이터 수집
def get_stock_data(symbol):
    try:
        stock = yf.Ticker(symbol)
        df = stock.history(period="6mo")
        if df.empty:
            print("yfinance에서 빈 데이터가 수신되었습니다.")
            return df
        return df[['Open', 'High', 'Low', 'Close', 'Volume']]
    except Exception as e:
        print(f"yfinance 데이터 수신 실패: {e}")
        return pd.DataFrame()

# 뉴스 수집
def get_news_data(query, max_results=5):
    url = f"https://www.googleapis.com/customsearch/v1?q={query}&cx={SEARCH_ENGINE_ID}&key={GOOGLE_API_KEY}&num={max_results}&hl=ko&gl=kr"
    response = requests.get(url)

    if response.status_code == 200:
        search_results = response.json().get('items', [])
        articles = []
        seen_titles = set()

        for result in search_results:
            title = result.get('title', '').strip()
            snippet = result.get('snippet', '').strip()
            if title and title not in seen_titles:
                articles.append({
                    'title': title,
                    'link': result.get('link'),
                    'description': snippet
                })
                seen_titles.add(title)
        return articles
    else:
        print("뉴스 API 오류:", response.status_code, response.text)
        return [{
            'title': "뉴스 불러오기 실패",
            'link': "#",
            'description': "일시적으로 뉴스 API 요청에 실패했습니다. 나중에 다시 시도해주세요."
        }]

# 뉴스 텍스트 저장
def save_news_to_txt(articles, filename="news.txt"):
    dir_name = os.path.dirname(filename)
    if dir_name:
        os.makedirs(dir_name, exist_ok=True)
    with open(filename, 'w', encoding='utf-8') as file:
        for article in articles:
            file.write(f"Title: {article['title']}\n")
            file.write(f"Description: {article['description']}\n")
            file.write(f"Link: {article['link']}\n\n")
