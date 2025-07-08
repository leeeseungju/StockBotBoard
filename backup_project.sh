#!/bin/bash

# 📦 백업 대상 디렉토리
PROJECT_NAME="StockBotBoard"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
OUTPUT_NAME="${PROJECT_NAME}_backup_${TIMESTAMP}"

# 🧹 제외할 항목 목록
EXCLUDES=(
  --exclude='.env'
  --exclude='StockBot/.venv'
  --exclude='__pycache__'
  --exclude='charts'
  --exclude='.git'
  --exclude='*.zip'
  --exclude='*.tar.gz'
)

# 📂 현재 경로 기준으로 압축
tar -czvf ${OUTPUT_NAME}.tar.gz "${EXCLUDES[@]}" .

echo "✅ 백업 완료: ${OUTPUT_NAME}.tar.gz"
