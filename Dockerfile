FROM ubuntu:22.04
LABEL authors="janshaka"
RUN apt-get update && \
    apt-get install -y \
    git \
    openjdk-17-jdk
RUN git clone https://github.com/shakajan/sogongjun-back-springboot.git /app
WORKDIR /app
RUN ls /app && chmod +x gradlew
RUN ./gradlew build -x test
ENTRYPOINT ["java", "-jar", "build/libs/sogonjun-spring-server-0.0.1-SNAPSHOT.jar"]
