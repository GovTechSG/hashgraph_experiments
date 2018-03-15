# Deployment

#### PreRequsites
1. <b>Unzip:</b> Make sure unzip is installed on the target server, if it is not installed, install it with <br />```sudo apt-get install unzip```<br />
2. <b>Docker (version 17.12.1-ce, build 7390fc6) </b> Make sure docker is installed on the target server, if not installed, install it with the instructions given in the below website<br />
```https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-16-04```
3. <b>Docker compose (version docker-compose version 1.19.0, build 9e633ef):</b> Make sure docker compose is installed on the target server, if not installed, install with <br/> 
```sudo curl -L https://github.com/docker/compose/releases/download/1.19.0/docker-compose-`uname -s`-`uname -m` -o /usr/bin/docker-compose```<br/>
```sudo chmod +x /usr/bin/docker-compose``` <br/>
```docker-compose --version``` : should return ```docker-compose version 1.19.0, build 9e633ef```


#### Steps for Deployment in ubuntu 16.04

1) SSh to the target server eg: ssh blockchain@xxx.xxx.xxx.xxx
2) Download and unzip the repo with the commands: <br />
```wget https://github.com/GovTechSG/hashgraph_experiments/archive/master.zip```
```unzip master.zip```
3) Build and run the project <br />
```cd hashgraph_experiments-master/```
```sudo docker-compose up```


