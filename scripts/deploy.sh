#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/moimtogether
cd $REPOSITORY

APP_NAME=moimtogether
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z $CURRENT_PID ]
then
  echo "> 종료할 애플리케이션이 없습니다."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> Deploy - $JAR_PATH "
nohup java -jar -Dspring.profiles.active=prod $JAR_PATH 1> /home/ubuntu/nohup.out 2>&1 & #백그라운드 실행