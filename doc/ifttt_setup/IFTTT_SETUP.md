# IFTTT Setup

This is optional and will send you all messages you would get by using the Slack/Telegram/Sendgrid/Gmail integration.
To add this functionality
these steps are needed:
1. Create an ifttt Account
2. Create an applet
3. Fill in the config.properties
4. Download the App for your phone

### 1. Create an account
Open this https://ifttt.com/join and create an account.

### 2. Create an applet
Open this https://ifttt.com/home and press the big `+ CREATE`.  
Click the `Add` at the If This section. Search for `Webhooks` and choose it. Next you choose the only available option.  
For the `Event Name` you need to choose something like `notify`.  
Click the `Add` at the Then That section. Search for `Notifications` and choose the one with the Bell icon.  
Select `Send a rich notification from the IFTTT app`.  
For the title choose something like `New free slot available`. This will be the title of the notification on your phone.  
For the message section please remove anything in it and click `Add ingredient` and choose `Value1`.  
This is important otherwise you don't get the information from the bot.  
Click the `Create action` button and then the `Continue` button followed by the `Finished` button.  
  
Open https://ifttt.com/maker_webhooks and click on `Documentation`.
Copy over the key. This is you API key. Don't share it with anyone!!!

### 3. Fill in the config.properties

Fill in all ifttt fields in the [config.properties](/src/main/resources/config.properties) and you're done

### 4. Download the App for your phone
Click [here](https://play.google.com/store/apps/details?id=com.ifttt.ifttt) if you have an Android phone.  
Click [here](https://apps.apple.com/de/app/ifttt/id660944635) if you have an iPhone.  
  
Log into your IFTTT Account on your phone and enable notifications!
