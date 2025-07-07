import os
from fastapi import FastAPI
from pydantic import BaseModel
from fastapi.staticfiles import StaticFiles
from fastapi.responses import JSONResponse
from langchain_community.llms import Ollama
import pandas as pd
import traceback

from data_fetcher import get_code_by_name, get_stock_data, get_news_data, save_news_to_txt
from report_generation import generate_report, save_summary_chart

SPRING_STATIC_CHART_DIR = "/app/charts"

app = FastAPI()

app.mount("/charts", StaticFiles(directory=SPRING_STATIC_CHART_DIR), name="charts")
app.mount("/static", StaticFiles(directory="static"), name="static")

# ✅ POST 요청용 Pydantic 모델
class ReportRequest(BaseModel):
    message: str

@app.post("/report")
def generate_combined_report(request: ReportRequest):
    print("✅ /report 요청 도착")  # 디버그 로그
    try:
        stock_name = request.message.strip()

        for suffix in ["알려줘", "어때?", "어때", "요약해줘", "종목 알려줘"]:
            stock_name = stock_name.replace(suffix, "").strip()

        stock_code = get_code_by_name(stock_name)
        if not stock_code:
            return {"error": f"⚠️ 종목 [{stock_name}]의 코드를 찾을 수 없습니다."}

        df = get_stock_data(stock_code)
        if df.empty:
            return {
                "error": f"⚠️ Yahoo Finance에서 '{stock_code}' 데이터를 불러오지 못했습니다. "
                         "종목은 존재하지만 API 제약으로 차트 불러오기에 실패했습니다."
            }

        summary_chart = save_summary_chart(stock_code, df)

        llm = Ollama(model="gemma:7b", base_url="http://ollama:11434")
        summary = llm.invoke(f"{stock_name}({stock_code})의 최근 주가 흐름을 초보자도 이해할 수 있게 요약해줘.")

        
        report_result = generate_report(stock_code, stock_name)
        print("✅ 리포트 생성 완료")
        return {
            "name": stock_name,
            "symbol": stock_code,
            "price": df["Close"].iloc[-1],
            "summary": summary,
            "summaryChart": summary_chart,
            "report": report_result["report"],
            "techChart": report_result["techChart"]
        }

    except Exception as e:
        print("❌ 예외 발생:")
        traceback.print_exc()
        return JSONResponse(status_code=500, content={"error": str(e)})

# if __name__ == "__main__":
#     import uvicorn
#     uvicorn.run("fastapi_app:app", host="0.0.0.0", port=8000)
