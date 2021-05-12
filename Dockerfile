FROM debian:buster

WORKDIR /app

RUN apt-get update -y

RUN DEBIAN_FRONTEND=noninteractive apt-get install -y unzip wget git default-jdk chromium=90.0.4430.93-1~deb10u1 xorg tightvncserver autocutsel lxde-core novnc python-websockify 

RUN wget https://chromedriver.storage.googleapis.com/90.0.4430.24/chromedriver_linux64.zip

RUN unzip chromedriver_linux64.zip

RUN rm chromedriver_linux64.zip

RUN wget https://services.gradle.org/distributions/gradle-7.0-bin.zip  -P /tmp/

RUN unzip -d /opt/gradle /tmp/gradle-7.0-bin.zip

RUN rm -rf /tmp/gradle-7.0-bin.zip

RUN git clone https://github.com/c0rrre/impf-bot.git

RUN mv impf-bot/* .

RUN rm -rf impf-bot/

RUN sed -i -e 's/\ \ \ \ val\ chromeDriver\ =\ ChromeDriver(chromeOptions)/\ \ \ \ System.setProperty("webdriver.chrome.whitelistedIps", "");\n\ \ \ \ chromeOptions.addArguments\("--no-sandbox"\);\n\ \ \ \ chromeOptions.addArguments\("--disable-dev-shm-usage"\);\n\ \ \ \ val\ chromeDriver\ =\ ChromeDriver\(chromeOptions\)/g' src/main/kotlin/de/tfr/impf/selenium/DriverFactory.kt

RUN cat src/main/kotlin/de/tfr/impf/selenium/DriverFactory.kt

RUN /opt/gradle/gradle-7.0/bin/gradle build

RUN cat src/main/kotlin/de/tfr/impf/selenium/DriverFactory.kt

RUN echo "# XScreenSaver Preferences File\nmode:		off\nselected:	-1" > /root/.xscreensaver

RUN cat /root/.xscreensaver

RUN mkdir /root/.vnc/

RUN echo "#!/bin/sh\n/usr/bin/autocutsel -s CLIPBOARD -fork\nxrdb $HOME/.Xresources\nxsetroot -solid grey\n#x-terminal-emulator -geometry 80x24+10+10 -ls -title \"$VNCDESKTOP Desktop\" &\n#x-window-manager &\n# Fix to make GNOME work\nexport XKL_XMODMAP_DISABLE=1\n/etc/X11/Xsession &\nx-terminal-emulator -e \"/opt/gradle/gradle-7.0/bin/gradle run 2>&1 | tee /app/impflog\"" > /root/.vnc/xstartup
RUN chmod +x /root/.vnc/xstartup

RUN cat /root/.vnc/xstartup

RUN mv /usr/share/novnc/vnc.html /usr/share/novnc/index.html

RUN echo "#!/bin/bash\necho -n \${VNC_PASSWORD:-impf-bot} | vncpasswd -f > /root/.vnc/passwd\nchmod 400 ~/.vnc/passwd\n\nexport USER=root\nvncserver :1 -geometry 1920x1080 -depth 24 && websockify -D --web=/usr/share/novnc/ 6901 localhost:5901\ntail -f /app/impflog" > /root/vnc-startup.sh
RUN chmod +x /root/vnc-startup.sh

RUN cat /root/vnc-startup.sh

RUN chmod go-rwx /root/.vnc

EXPOSE 5901
EXPOSE 6901

CMD ["/root/vnc-startup.sh"]

#ENTRYPOINT ["/opt/gradle/gradle-7.0/bin/gradle", "run" ]
