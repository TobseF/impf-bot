# 💉🤖 Impf-Bot

[![Kotlin](https://img.shields.io/badge/Kotlin-1.4.32-blue.svg?style=flat&logo=kotlin&logoColor=white)](http://kotlinlang.org)
[![Java](https://img.shields.io/badge/Java-14-red.svg?style=flat&logo=Java&logoColor=white)](https://adoptopenjdk.net)
[![Gradle](https://img.shields.io/badge/Gradle-7.0.0-08313A.svg?style=flat&logo=Java&logoColor=white)](https://gradle.org)
[![Selenium](https://img.shields.io/badge/Selenium-3.141.59-green.svg?style=flat&logo=Selenium&logoColor=white)](https://www.selenium.dev/)

Searches the official "[ImpfterminService - Der Patientenservice 116117](https://www.impfterminservice.de/)" for free
Corona vaccination slots. It can search multiple locations at once which can be provided in a list. When it finds a free slot, it
can use Slack to send you a message. All timings can be configured for your needs and to avoid blocking.

📱 Also the SMS verification step is managed by a Slack bot. Simply write the verification code into the Slack channel:
```properties
sms:999-999
```

## ⭐ Features
 ⭐ Full browser automation   
 ⭐ No API Hacks  
 ⭐ Telegram integration  
 ⭐ Slack integration  
 ⭐ Waiting room detection   
 ⭐ Customizable by property file  
 ⭐ Docker support to run it locally or in the cloud


![Sequence Digramm](doc/sequence-doku.png)
These steps are repeated for every location. So it's no problem to check 14 locations at once.
If the bot recognizes that no free slots are available, it waits 30 seconds and goes on to the next location.

> ### ⚠ Warning: The online booking isn't an authorization
> On the booking date you still have to bring the documents with you, to proof that you are qualified to receive the vaccination.
> Check out [the official guidelines](https://sozialministerium.baden-wuerttemberg.de/de/gesundheit-pflege/gesundheitsschutz/infektionsschutz-hygiene/informationen-zu-coronavirus/impfberechtigt-bw/)
> and make sure you are qualified for them. This bot doesn't help you get a privilege. It only allows you to get a date without losing the nerves or waisting a lifetime in pointless callcenter calls.

### What is Slack and do I need it?
This bot can also work without it. [Slack](https://slack.com/) is a messaging application for companies.
It is useful to get realtime updates from the bot and to do the SMS verification without the need to be in front of your computer.
Otherwise, when the verification starts, you have only 10 minutes left to type the code into the automated browser window.
If you take a journey through the dangerous outside world, this may be unpractical. I have chosen Slack, because it proves a nice Java API
and it was already running in our company.

## 🛠 Setup

### Java
To build and run the bot you need at least a Java 14 installation.

### Selenium

The Impf-Bot uses [Selenium](https://www.selenium.dev) to automate a webbrowser. Selenium requires a locally installed
driver and browser - Chrome is recommended. You can manually download the latest selenium drivers
on [GitHub - ChromeDriver](https://github.com/SeleniumHQ/selenium/wiki/ChromeDriver)
or use this Windows bash script to do it for you:

```shell
@"%SystemRoot%\System32\WindowsPowerShell\v1.0\powershell.exe" -NoProfile -InputFormat None -ExecutionPolicy Bypass -Command "iex ((New-Object System.Net.WebClient).DownloadString('https://chocolatey.org/install.ps1'))" && SET "PATH=%PATH%;%ALLUSERSPROFILE%\chocolatey\bin"
choco install selenium-chrome-driver
```

### `config.properties`
Settings are stored in a config file which is located in: `src/main/resources/config.properties`.  
Edit these settings before the first run. A missing boolean property will be treated as `false`.

```properties
mainPageUrl = https://www.impfterminservice.de/impftermine
# Comma separated list of locations. 
# Optional, if you already have a placement code, just add it in square brackets after the place 
locations = 69124 Heidelberg[XXXX-XXXX-XXXX],76137 Karlsruhe

# Waiting time before checking the next location in seconds [s]
waitingTime = 120
# Waiting time for a manual user interaction in minutes [m]
waitingTimeForUserAction = 15
# Waiting time in Waiting Room in minutes [m]
waitingTimeInWaitingRoom = 15
# Timeout when searching an element on the page in seconds [s]
searchElementTimeout = 16
# Explicit waiting time for browser updates in seconds [s]
waitingTimeForBrowser = 2

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
# Also read the SMS back from a channel. Needs the slackBotChannelReadSms-Name and Id.
readSmsFromSlack = false
slackBotChannelReadSmsName = #smsgameway
slackBotChannelReadSmsId = CE99999PY
```

### Setup Slack
This step is optional. You can find detailed setup instructions here: [SlackBot Setup](/doc/slack_setup/SLACK_SETUP.md).

### Setup Telegram
This step is optional. You can find detailed setup instructions here: [TelegramBot Setup](/doc/telegram_setup/TELEGRAM_SETUP.md).

### Docker
This step is optional. To run this tool in Docker, follow this tutorial: [Docker](/doc/docker/DOCKER_SETUP.md).

### `simplelogger.properties`
Per default the bot logs only successful bookings.    
To change the Log-Level edit the `defaultLogLevel`:  
`org.slf4j.simpleLogger.defaultLogLevel = info`  
| ℹ Change the log level to `debug` to get a more detailed output during every step.

## 🔨 Build

```shell
gradle build
```
The build creates runnable Java-fat-jar which contains all dependencies and the config:  
`build/libs/impf-bot-1.0-SNAPSHOT-all.jar`

## 🚀 Start

To run the bot, simply use the gradle command:
```shell
gradle run
```

As an alternative, you can also start it with the fat-jar in the command line by:
```shell
java -jar build/libs/impf-bot-1.0-SNAPSHOT-all.jar 
```
