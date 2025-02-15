# ✅ Amazon Corretto 17을 사용하여 Java 실행 환경 구성
FROM amazoncorretto:17

# ✅ 작업 디렉토리 생성
WORKDIR /app

# ✅ JAR 파일 복사
COPY build/libs/*.jar app.jar

# ✅ 컨테이너가 실행될 때 사용할 포트 지정
EXPOSE 8080

# ✅ 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
