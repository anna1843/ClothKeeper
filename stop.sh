#!/usr/bin/env bash

PROJECT_ROOT="/home/ubuntu/app/build/libs"
JAR_FILE="$PROJECT_ROOT/dev-0.0.1-SNAPSHOT.jar"



CURRENT_PID=$(pgrep -f $JAR_FILE)

echo "$CURRENT_PID"

# 프로세스가 켜져 있으면 종료
if [ -z $CURRENT_PID ]; then
  echo "현재 실행중인 애플리케이션이 없습니다"
  # jar 파일 실행
  echo "$CURRENT_PID"
else
  echo "실행 중인 애플리케이션 종료 "
  kill -9 $CURRENT_PID
  sleep 5
fi


echo "파일 실행"

# jar 파일 실행
echo "$CURRENT_PID"

echo "$JAR_FILE"

java -jar $JAR_FILE