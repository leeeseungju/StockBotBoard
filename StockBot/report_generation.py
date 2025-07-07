import os
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
from datetime import datetime
from pathlib import Path
import torch
from matplotlib import font_manager, rc
import mplfinance as mpf
from fastapi import FastAPI, Query
from fastapi.staticfiles import StaticFiles

from config import ALPHA_VANTAGE_API_KEY, GOOGLE_API_KEY, SEARCH_ENGINE_ID, HUGGINGFACEHUB_API_TOKEN
from data_fetcher import get_stock_data, get_news_data, save_news_to_txt

from langchain_core.prompts import PromptTemplate
from langchain_core.runnables import RunnableSequence
from langchain_community.llms import Ollama
from langchain_community.vectorstores import Chroma
from langchain_community.embeddings import SentenceTransformerEmbeddings
from langchain_community.document_loaders import TextLoader
from langchain.text_splitter import CharacterTextSplitter
from langchain_community.embeddings import HuggingFaceEmbeddings
from langchain_community.vectorstores import FAISS

SPRING_STATIC_CHART_DIR = "/app/charts"  # charts 저장 경로

app = FastAPI()

app.mount("/charts", StaticFiles(directory=SPRING_STATIC_CHART_DIR), name="charts")

# BASE_DIR = os.path.dirname(os.path.abspath(__file__))
# SPRING_STATIC_CHART_DIR = os.path.join(BASE_DIR, "charts")

# 한글 폰트 설정
font_path = "/usr/share/fonts/truetype/nanum/NanumGothic.ttf"
font_name = font_manager.FontProperties(fname=font_path).get_name()
rc('font', family=font_name)

# torch 오류 회피용
import types
if isinstance(getattr(torch, 'classes', None), types.ModuleType):
    if not hasattr(torch.classes, '__path__'):
        torch.classes.__path__ = []

def add_technical_indicators(df):
    df['sma20'] = df['Close'].rolling(window=20).mean()
    df['ema20'] = df['Close'].ewm(span=20).mean()
    low_min = df['Low'].rolling(window=5).min()
    high_max = df['High'].rolling(window=5).max()
    df['fast_k'] = (df['Close'] - low_min) / (high_max - low_min) * 100
    df['slow_k'] = df['fast_k'].rolling(window=3).mean()
    df['slow_d'] = df['slow_k'].rolling(window=3).mean()
    return df

def get_technical_summary(df):
    latest = df.iloc[-1]
    return (
        f"SMA(20): {latest['sma20']:.2f}\n"
        f"EMA(20): {latest['ema20']:.2f}\n"
        f"스토캐스틱 %K: {latest['slow_k']:.2f}\n"
        f"스토캐스틱 %D: {latest['slow_d']:.2f}"
    )

def plot_technical_chart(df, stock_symbol):
    import matplotlib.pyplot as plt
    import os

    plt.figure(figsize=(14, 6))

    plt.subplot(2, 1, 1)
    plt.plot(df['Close'], label='Close', color='black')
    plt.plot(df['sma20'], label='SMA 20', linestyle='--')
    plt.plot(df['ema20'], label='EMA 20', linestyle=':')
    plt.title(f"{stock_symbol} 주가 및 이동평균선")
    plt.legend()
    plt.grid()

    plt.subplot(2, 1, 2)
    plt.plot(df['slow_k'], label='Slow %K', color='blue')
    plt.plot(df['slow_d'], label='Slow %D', color='red')
    plt.axhline(80, color='gray', linestyle='--')
    plt.axhline(20, color='gray', linestyle='--')
    plt.title("Stochastic Oscillator")
    plt.legend()
    plt.grid()

    os.makedirs(SPRING_STATIC_CHART_DIR, exist_ok=True)
    filename = f"{stock_symbol.replace('.', '_')}_tech_chart.png"
    save_path = os.path.join(SPRING_STATIC_CHART_DIR, filename)

    print(f"기술 차트 저장 위치: {save_path}")
    plt.tight_layout()
    plt.savefig(save_path)
    plt.close()
    print("차트 저장 완료")

    return filename


def generate_report(stock_symbol, stock_name):
    stock_data = get_stock_data(stock_symbol)
    if stock_data.empty:
        return {"report": "주가 데이터를 불러오지 못했습니다.", "techChart": ""}

    stock_data = add_technical_indicators(stock_data)
    tech_summary = get_technical_summary(stock_data)
    latest_close = stock_data['Close'].iloc[-1]
    latest_close_date = stock_data.index[-1].strftime('%Y-%m-%d')

    news_articles = get_news_data(stock_name)
    save_news_to_txt(news_articles, filename="news.txt")

    news_info = f"뉴스 불러오기 실패 ({datetime.today().strftime('%Y-%m-%d')})"
    if os.path.exists("news.txt") and os.path.getsize("news.txt") > 0:
        loader = TextLoader("news.txt", encoding='utf-8')
        docs = loader.load()
        chunks = CharacterTextSplitter(chunk_size=500, chunk_overlap=100).split_documents(docs)

        embeddings = HuggingFaceEmbeddings(
            model_name="sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2",
            model_kwargs={"device": "cpu"}
        )
        vectordb = FAISS.from_documents(chunks, embeddings)

        if news_articles:
            news_info = "뉴스 요약 완료"
            news_links = "<br>".join(
                f'<a href="{a["link"]}" target="_blank">{a["title"]}</a>' for a in news_articles
            )
        else:
            news_links = "뉴스 링크 없음"
    else:
        news_links = "뉴스 데이터 없음"

    prompt_template = PromptTemplate(
        input_variables=["stock_name", "stock_symbol", "latest_close", "latest_close_date", "news_info", "tech_summary"],
        template="""당신은 한국어로 종목 분석 리포트를 작성하는 증권사 애널리스트입니다. 아래 정보를 바탕으로 **전체 리포트를 한국어로 작성**해 주세요.

        1. 종목개요
        2. 시장 동향 및 주요 뉴스
        3. 재무 분석
        4. 기술적 분석 요약  
        {tech_summary}

        5. 리스크 요인
        6. 결론 및 투자 의견

        종목명: {stock_name}  
        종목코드: {stock_symbol}  
        최신 종가: {latest_close} USD (기준일: {latest_close_date})  
        주요 뉴스 요약:  
        {news_info}
        """
    )

    llm = Ollama(model="gemma:7b", base_url="http://ollama:11434")
    chain = prompt_template | llm
    report_text = chain.invoke({
        "stock_name": stock_name,
        "stock_symbol": stock_symbol,
        "latest_close": latest_close,
        "latest_close_date": latest_close_date,
        "news_info": news_info,
        "tech_summary": tech_summary
    })

    report_text += f"<br><br><b>📎 참고 뉴스 링크</b><br>{news_links}"

    techChart = plot_technical_chart(stock_data, stock_symbol)

    return {
        "report": report_text,
        "techChart": techChart
    }

def save_summary_chart(stock_symbol, df):
    import matplotlib.pyplot as plt
    import os

    os.makedirs(SPRING_STATIC_CHART_DIR, exist_ok=True)
    filename = f"{stock_symbol.replace('.', '_')}_summary_chart.png"
    save_path = os.path.join(SPRING_STATIC_CHART_DIR, filename)

    print(f"요약 차트 저장 위치: {save_path}")
    plt.figure(figsize=(10, 4))
    plt.plot(df["Close"], label="종가", color="blue")
    plt.title(f"{stock_symbol} 주가 추이")
    plt.xlabel("날짜")
    plt.ylabel("주가")
    plt.legend()
    plt.tight_layout()
    plt.savefig(save_path)
    plt.close()
    print("요약 차트 저장 완료")

    return filename
