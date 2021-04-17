# 💉🤖 Impf-Bot

[![Kotlin](https://img.shields.io/badge/Kotlin-1.4.32-blue.svg?style=flat&logo=kotlin&logoColor=white)](http://kotlinlang.org)
[![Java](https://img.shields.io/badge/Java-14-red.svg?style=flat&logo=Java&logoColor=white)](https://adoptopenjdk.net)
[![Gradle](https://img.shields.io/badge/Gradle-6.8.3-08313A.svg?style=flat&logo=Java&logoColor=white)](https://gradle.org)
[![Selenium](https://img.shields.io/badge/Selenium-3.141.59-green.svg?style=flat&logo=Selenium&logoColor=white)](https://www.selenium.dev/)

Searches the official "[ImpfterminService - Der Patientenservice 116117](https://www.impfterminservice.de/)" for free
Corona vaccinate slots. It can search multiple locations which can be provided in a list. When it finds a free slot, it
can use Slack to send you a message.

## 🛠 Setup

### Selenium

The Impf-Bot uses [Selenium](https://www.selenium.dev) to automate a webbrowser. Selenium requires a locally installed
driver and browser - Chrome is recommended. You can manually download the leastest selenium drivers
on [GitHub - ChromeDriver](https://github.com/SeleniumHQ/selenium/wiki/ChromeDriver)
or use this Windows bash script to do it for you:

```shell
@"%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -InputFormat None -ExecutionPolicy Bypass -Command "iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))" && SET "PATH=%PATH%;%ALLUSERSPROFILE%\chocolatey\bin"
choco install selenium-chrome-driver
```

### `config.properties`
Settings are stored in a config file which is located in: `src/main/resources/config.properties`.  
Edit these settings before first start.

```properties
mainPageUrl = https://www.impfterminservice.de/impftermine
# Comma separated list of locations
locations = 69123 Heidelberg,76137 Karlsruhe
# Your age. Used in age verification field.
personAge = 42

# Settings for the request page. Only needed if you set `sendRequest = true`  
sendRequest = true
email = impfMePlease@lasthope.de
# Mobile number for sms verification. Numbers after the "+49"
mobileNumber = 152123123123

# Chrome Driver
pathDriver = C:/ProgramData/chocolatey/lib/chromedriver/tools/
nameDriver = webdriver.chrome.driver
exeDriver = chromedriver.exe

# Enabled the slack messages
slackEnabled = false
# These can be skipped, if Slack is `slackEnabled = false`
slackBotApiToken = xoxb-123123123-123123123123123123123123123
slackBotChannel = #random
```

### `simplelogger.properties`

To change the Log-Level edit the `defaultLogLevel`:  
`org.slf4j.simpleLogger.defaultLogLevel = info`

## 🔨 Build

```shell
gradle build
```

## 🚀 Start

```shell
gradle run
```
