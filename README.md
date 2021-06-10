# 💉🤖 Impf-Bot

[![Kotlin](https://img.shields.io/badge/Kotlin-1.4.32-blue.svg?style=flat&logo=kotlin&logoColor=white)](http://kotlinlang.org)
[![Java](https://img.shields.io/badge/Java-14-red.svg?style=flat&logo=Java&logoColor=white)](https://adoptopenjdk.net)
[![Gradle](https://img.shields.io/badge/Gradle-7.0.2-08313A.svg?style=flat&logo=Java&logoColor=white)](https://gradle.org)
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

## 🤩 Thank You
This bot exits and became so useful because of the over 20 people who contributed to this project.
That is strength of open-source power 💪  
![zaanposni](https://avatars.githubusercontent.com/u/24491035?v=4&s=42) ![fabicodes](https://avatars.githubusercontent.com/u/1745612?v=4&s=42) ![atpanos](https://avatars.githubusercontent.com/u/24278584?v=4&s=42) ![philib](https://avatars.githubusercontent.com/u/7396985?v=4&s=42) ![s01iD](https://avatars.githubusercontent.com/u/811984?v=4&s=42) ![superflo22](https://avatars.githubusercontent.com/u/26706262?v=4&s=42) ![seppi91](https://avatars.githubusercontent.com/u/4396900?v=4&s=42) ![alfonsrv](https://avatars.githubusercontent.com/u/48770755?v=4&s=42) ![schniggie](https://avatars.githubusercontent.com/u/637619?v=4&s=42) ![h0nIg](https://avatars.githubusercontent.com/u/2757302?v=4&s=42) ![Timwun](https://avatars.githubusercontent.com/u/13067978?v=4&s=42) ![TobseF](https://avatars.githubusercontent.com/u/1190109?v=4&s=42)  
So any thanks has to go to all contributors.  
If anybody still wants to honor the initial publish you can [buy Tobse a choco donut](https://www.buymeacoffee.com/tobse) 🍩. 

## 🧭 Overview
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
driver and browser - Chrome is recommended. The download of the chromedriver is done automatically via [WebDriverManager](https://github.com/bonigarcia/webdrivermanager).

### `config.properties`
Settings are stored in a config file which is located in: `src/main/resources/config.properties`.  
Edit these settings before the first run. A missing boolean property will be treated as `false`.

```properties
mainPageUrl = https://www.impfterminservice.de/impftermine
# Name of the state from the select list
state = Baden-Württemberg
# Comma separated list of locations. Optional, if you already have a placement code just add it in square brackets after the place. Since placement codes are not related to locations but to servers you can furthermore optionally specify the related server code next to the placement code in parentheses. The server code can be found in the URL e.g. "001-iz.impfterminservice.de" -> server code == "001".
locations = 69124 Heidelberg[XXXX-XXXX-XXXX](XXX),76137 Karlsruhe

# Birth date, used for age verification (which also changes the available vaccines).
birthDate = 31.12.1994

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

# Settings for the request page. Only needed if you set `sendRequest = true`
sendRequest = false
email = impfMePlease@lasthope.de
# Mobile number for sms verification. Numbers after the "+49"
mobileNumber = 152123123123

# Enabled the Slack messages
slackEnabled = false
# These can be skipped, if Slack is disabled
slackBotApiToken = xoxb-123123123-123123123123123123123123123
slackBotChannel = #random
# Also read the SMS back from a channel. Needs the slackBotChannelReadSms-Name and Id.
readSmsFromSlack = false
slackBotChannelReadSmsName = #smsgameway
slackBotChannelReadSmsId = CE99999PY

# Enabled the Telegram messages
telegramEnabled = false
# These can be skipped, if Telegram is disabled
telegramApiToken = 10000001:AAEMkawDRi5-_yX7fr-pxxxxxxxxx-2EOE
telegramBotUsername = @iufdhsgouihsdfgbot
telegramChatId = 1231231231

# Enabled the Twilio Sendgrid email messages
sendgridEnabled = false
# These can be skipped, if Sendgrid is disabled
sendgridApiToken = SG.aaaaaaaaaaaaaaaaaaaaaa.bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb
sendgridEmailFrom = from@email.com
sendgridEmailTo = to@email.com

# Enabled the Gmail email messages
gmailEnabled = false
# These can be skipped, if Gmail is disabled
gmailAppPassword = xxxxxxxxxxxxxxxx
gmailEmailFrom = from@gmail.com
# Can be the same as the from email
gmailEmailTo = to@email.com

# Enabled the IFTTT notifications
iftttEnabled = false
# These can be skipped, if IFTTT is disabled
iftttApiKey = xxxxxxxxxxxxxxxx
iftttEventName = notify

# Enabled alarm sound.
# Alarm will be played on start and in addition to an enabled notification service.
alarmEnabled = false
# Alarm duration in msec
alarmDuration = 2000
# Tone frequency in hz
alarmFrequency = 800
# Volume in percentage (0.0-1.0)
alarmVolumeInPercentage = 0.5

# Automatically selects the first possible vaccination date and enters all required personal data based on the following values to book this vaccination slot for you.
bookingEnabled = false
# Takes screenshots during booking process to have proof of you booking. (Optional)
takeScreenshots = false
# Defines the path were all screenshots will be stored in (Optional). Files will be prefixed with "impf-bot-"
outputPath = /home
# Personal Data
# Salutation (m=man, w=women, d=divers, c=child)
personalDataSalutation = m
# Firstname
personalDataFirstname = Max
# Lastname
personalDataLastname = Mustermann
# Zipcode
personalDataZipcode = 69124
# City
personalDataCity = Heidelberg
# Street
personalDataStreet = Hauptstraße
# House number
personalDataHouseNumber = 911
# Phone number. Numbers after the "+49"
personalDataMobileNumber = 152123123123
# E-Mail
personalDataEmail = impfMePlease@lasthope.de

# It seems that non windows agents are blocked. You can set it to "default" to use the default one of chrome.
# Could be something like: "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36"
userAgent = default

# Clears browser cookies to avoid bot detection. Disabled by default.
clearCookies = false
# Experimental feature, which tries to clear the browser cache to avoid bot detection. Disabled by default.
experimentalClearBrowser = false
```

### Setup Slack
This step is optional. You can find detailed setup instructions here: [SlackBot Setup](/doc/slack_setup/SLACK_SETUP.md).

### Setup Telegram
This step is optional. You can find detailed setup instructions here: [TelegramBot Setup](/doc/telegram_setup/TELEGRAM_SETUP.md).

### Setup Gmail
This step is optional. You can find detailed setup instructions here: [Gmail Setup](/doc/gmail_setup/GMAIL_SETUP.md).

### Setup Ifttt
This step is optional. You can find detailed setup instructions here: [Ifttt Setup](/doc/ifttt_setup/IFTTT_SETUP.md).

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
