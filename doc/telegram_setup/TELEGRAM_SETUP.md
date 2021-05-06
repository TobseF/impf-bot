# 🤖🛠 Telegram Setup

This is optional and will send you all messages you would get by using the Slack integration.
To add this functionality 
these steps are needed:
1. add the BotFather
2. create a custom Bot
3. obtain your chatId

## Setup Telegram
### 1. Add the BotFather
Open this link https://t.me/botfather and send him `/start`.

### 2. Create a new Bot 
Send the BotFather `/newbot` and choose a name, this will show up as a chat name.
Now you have to give it some random name which also has to be set as 
`telegramBotUsername` in the [config.properties](/src/main/resources/config.properties). 
Next send `/token` and select your newly created bot. Take the HTTP API token and
set it as `telegramApiToken` in the config.

### 3. Obtain your chatId
#### Via your own Bot
Send @yourNewBot1334 a message, a simple Hello is enough.
Now replace `$(HTTP-TOKEN)` in this link `https://api.telegram.org/bot$(HTTP-TOKEN)/getUpdates` and
open it with a browser. Search for the "id" in this JSON response and save the number behind to the config.
#### Via @myidbot
Send a `/start` message to @myidbot (https://t.me/myidbot), followed by a `/getid` message. The bot will reply with your chat id. Put that to your config.
