FROM debian:buster

WORKDIR /app

RUN apt-get update -y

RUN apt-get install -y unzip wget git default-jdk chromium=90.0.4430.93-1~deb10u1

RUN wget https://chromedriver.storage.googleapis.com/90.0.4430.24/chromedriver_linux64.zip

RUN unzip chromedriver_linux64.zip

RUN rm chromedriver_linux64.zip

RUN wget https://services.gradle.org/distributions/gradle-7.0-bin.zip  -P /tmp/

RUN unzip -d /opt/gradle /tmp/gradle-7.0-bin.zip

RUN rm -rf /tmp/gradle-7.0-bin.zip

RUN git clone https://github.com/TobseF/impf-bot.git

RUN mv impf-bot/* .

RUN rm -rf impf-bot/

RUN /opt/gradle/gradle-7.0/bin/gradle build

RUN useradd -ms /bin/bash impfbot

RUN chown -R impfbot:impfbot /app

USER impfbot

RUN sed -i -e 's/\ \ \ \ val\ chromeDriver\ =\ ChromeDriver()/\ \ \ \ val\ options\ =\ ChromeOptions\(\)\n\ \ \ \ options.addArguments\("--headless"\)\n\ \ \ \ options.addArguments\("--no-sandbox"\);\n\ \ \ \ options.addArguments\("--disable-dev-shm-usage"\);\n\ \ \ \ val\ chromeDriver\ =\ ChromeDriver\(options\)/g' src/main/kotlin/de/tfr/impf/selenium/DriverFactory.kt

RUN sed -i -e 's/import\ mu.KotlinLogging/import\ mu.KotlinLogging\nimport\ org.openqa.selenium.chrome.ChromeOptions/g' src/main/kotlin/de/tfr/impf/selenium/DriverFactory.kt

ENTRYPOINT ["/opt/gradle/gradle-7.0/bin/gradle", "run" ]
