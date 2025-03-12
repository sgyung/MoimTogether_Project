#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/moimtogether
cd $REPOSITORY

APP_NAME=moimtogether
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

BLUE_PORT=8080
GREEN_PORT=8081
LOG_FILE=/home/ubuntu/deploy.log

echo "> 현재 실행 중인 애플리케이션 확인"

BLUE_PID=$(sudo lsof -ti :$BLUE_PORT)
GREEN_PID=$(sudo lsof -ti :$GREEN_PORT)

echo "현재 블루 PID: $BLUE_PID"
echo "현재 그린 PID: $GREEN_PID"

if [ -z "$BLUE_PID" ]; then
    TARGET_PORT=$BLUE_PORT
    BEFORE_PORT=$GREEN_PORT  # 롤백 시 사용할 포트
    echo "> 블루(8080)로 배포합니다."
elif [ -z "$GREEN_PID" ]; then
    TARGET_PORT=$GREEN_PORT
    BEFORE_PORT=$BLUE_PORT  # 롤백 시 사용할 포트
    echo "> 그린(8081)로 배포합니다."
else
    echo "블루와 그린이 모두 실행 중입니다."
    exit 1
fi

echo "> JAR 파일 배포 - $JAR_PATH"
nohup java -jar -Dspring.profiles.active=prod $JAR_PATH --server.port=$TARGET_PORT > /home/ubuntu/nohup.out 2>&1 &

# 배포 후 10초 대기 (애플리케이션 실행을 기다림)
sleep 10

# 포트가 열릴 때까지 최대 30초 대기.
for i in {1..30}
do
    NEW_PID=$(sudo lsof -ti :$TARGET_PORT)

    if [ ! -z "$NEW_PID" ]; then
        echo "> 애플리케이션 기동 확인됨 (PID: $NEW_PID)"
        break
    fi

    sleep 1

    if [ $i -eq 30 ]; then
        echo "> 애플리케이션이 30초 내에 기동되지 않음. 배포 실패"
        exit 1
    fi
done

# 헬스체크 시작 (최대 20초 대기)
for i in {1..20}
do
    sleep 1
    RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:$TARGET_PORT/actuator/health)

    if [ "$RESPONSE" == "200" ]; then
        echo "> 서비스 정상 기동 확인 (port: $TARGET_PORT)"
        break
    fi

    if [ $i -eq 20 ]; then
        echo "서비스 기동 실패 (port: $TARGET_PORT)"

        # 신규 서비스 종료 (실패한 버전 종료)
        NEW_PID=$(sudo lsof -ti :$TARGET_PORT)
        echo "> 신규 서비스 (PID: $NEW_PID)"
        if [ ! -z "$NEW_PID" ]; then
            echo "> 배포 실패로 신규 서비스 종료 (PID: $NEW_PID)"
            kill -15 $NEW_PID
        fi

        # Nginx 롤백 (기존 포트로 되돌리기)
        if [ "$TARGET_PORT" == "$BLUE_PORT" ]; then
            sudo sed -i 's/server 127.0.0.1:8080/server 127.0.0.1:8081/' /etc/nginx/sites-available/default
        else
            sudo sed -i 's/server 127.0.0.1:8081/server 127.0.0.1:8080/' /etc/nginx/sites-available/default
        fi

        sudo nginx -s reload
        echo "$(date +%Y-%m-%dT%H:%M:%S) 배포 실패 - Port: $TARGET_PORT, Profile: $TARGET_PROFILE, JAR: $JAR_NAME" >> $LOG_FILE
        exit 1
    fi
done

echo "> Nginx 연결 변경"
if [ "$TARGET_PORT" == "$BLUE_PORT" ]; then
    sudo sed -i 's/server 127.0.0.1:8081/server 127.0.0.1:8080/' /etc/nginx/sites-available/default
else
    sudo sed -i 's/server 127.0.0.1:8080/server 127.0.0.1:8081/' /etc/nginx/sites-available/default
fi

sudo nginx -s reload

# 이전 서비스 종료 (성공 시 반대쪽 서비스 종료)
if [ "$TARGET_PORT" == "$BLUE_PORT" ]; then
    if [ ! -z "$GREEN_PID" ]; then
        echo "> 기존 그린 서비스 종료"
        kill -15 $GREEN_PID
    fi
else
    if [ ! -z "$BLUE_PID" ]; then
        echo "> 기존 블루 서비스 종료"
        kill -15 $BLUE_PID
    fi
fi

echo "$(date +%Y-%m-%dT%H:%M:%S) 배포 완료 - Port: $TARGET_PORT, Profile: $TARGET_PROFILE, JAR: $JAR_NAME" >> $LOG_FILE
echo "> 배포 완료"
