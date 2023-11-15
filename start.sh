#!/usr/bin/env bash

APP_NAME="dev-0.0.1-SNAPSHOT.jar"
PROJECT_ROOT="/home/ubuntu/app/build/libs"
JAR_FILE="$PROJECT_ROOT/dev-0.0.1-SNAPSHOT.jar"


CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]; then
  echo "현재 실행중인 애플리케이션이 없습니다"
  echo "$CURRENT_PID"
else
  echo "실행중인애플리케이션 종료 "
  sudo kill -9 $JAR_FILE
fi


echo "jar 빌드"
nohup java -jar $JAR_FILE

