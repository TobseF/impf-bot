# 🐋🛠 Docker

## 1. Prepare config.properties

If you have an already existing config.properties, you can copy it to the root folder of the project for convenience, so you wont have to alter the following commands

If your dont have an existing file, follow the config.properties guide in the main [README.md](https://github.com/TobseF/impf-bot/blob/master/README.md) and copy it to the projects root folder.

<br>
Now open the file in your favourite editor and edit the Chromedriver options to match the working ones for the container:
<br><br>

```
PathDriver = /app
nameDriver = webdriver.chrome.driver
exeDriver = chromedriver
```
<br><br>
Now your are ready to go on with the quickstart or build the container yourself
<br><br>

## 2.1 Quickstart

To simply run the container pre-built, either execute

```docker run -e VNC_PASSWORD=CHANGEME --name impf-bot -d -p 5901:5901 -p 6901:6901 -v $PWD/config.properties:/app/src/main/resources/config.properties pfuenzle/impf-bot```

and replace ```$PWD/config.properties``` with the path and filename to your properties file (if config.properties is not in your current path).

Additionally, replace CHANGEME with a password for your VNC Server.
<br><br>

OR 
<br><br>

replace ```./config.properties``` in docker-compose.yml with the path and filename to your properties file (if config.properties is not in the same folder as docker-compose.yml).

After that, replace CHANGEME with the password for your VNC Server and then execute 

```docker-compose up -d```

in the root folder of the project. 


Both options deploy a running container with the name impf-bot in the background.
Congratulations, your container is now up- and running and searching for an appointment  

<br>

### UI  

The container has two open ports: 5901 (vnc) and 6901 (web).

If an appointment has been found or you want to check in on the process, 
either connect to ```127.0.0.1:5901``` with a VNC Client of your choice
or
open ```127.0.0.1:6901``` in any available webbrowser. This will give you full access to the webbrowser and allow you to input any data necessary.

If it is asking for a password, input the one you set in the ```docker run``` command or the ```docker-compose.yml```. If you removed the environment variable, the default password is ```impf-bot```

<br><br>
To get logs from the container:

```docker logs impf-bot```

To stop the container:

```docker stop impf-bot && docker rm impf-bot```
<br><br>

## 2.2 Build container yourself

To build the container yourself, go to the projects root folder and execute

```docker build . -t impf-bot```

After successfully building the container, follow the Quickstart guide, but replace 

```pfuenzle/impf-bot``` with ```impf-bot```, to use your local build instead of the prebuilt version
