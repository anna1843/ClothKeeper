#!/usr/bin/env bash

APP_NAME="dev-0.0.1-SNAPSHOT.jar"
PROJECT_ROOT="/home/ubuntu/app/build/libs"
JAR_FILE="$PROJECT_ROOT/$APP_NAME"

TIME_NOW=$(date +%c)

CURRENT_PID=$(pgrep -f "$APP_NAME")

# 프로세스가 켜져 있으면 종료
if [ -z "$CURRENT_PID" ]; then
  echo "현재 실행중인 애플리케이션이 없습니다"
else
  echo "현재 실행중인 애플리케이션이 제거"
    sudo kill -15 "$CURRENT_PID"
    sleep 5
fi

# jar 파일 실행
nohup java -jar "$JAR_FILE" &
