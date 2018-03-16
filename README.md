# Deployment in Ubuntu Server 16.04 LTS

## 1. Deployment of multiple nodes in a single server

#### Server setup

1. Set up the servers in the same vnet inorder for them to communicate with each other

2. Open the port 52204-52207, 8080 in the server by specifying the inbound and outbound ports to 52204 and 8080. </br>
8080 is for swagger <br/>
52204-52207 is for hashgraph nodes <br/>

##### Note: Tested in server with the below configuration
![alt text](https://github.com/GovTechSG/hashgraph_experiments/blob/readme/images/azure-servers.png)


#### PreRequsites
1. <b>Unzip:</b> Make sure unzip is installed on the target server, if it is not installed, install it with <br />```sudo apt-get install unzip```<br />
2. <b>Docker (version 17.12.1-ce, build 7390fc6) </b> Make sure docker is installed on the target server, if not installed, install it with the instructions given in the below website<br />
```https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-16-04```
###### Note: Complete Until step: 1 (Step 1 — Installing Docker) <br/>
Make sure docker is installed properly with the command ```sudo docker version```. Expected output: <br/>
![alt text](https://github.com/GovTechSG/hashgraph_experiments/blob/readme/images/docker-version.png)

3. <b>Docker compose (version docker-compose version 1.19.0, build 9e633ef):</b> Make sure docker compose is installed on the target server, if not installed, install with <br/> 
```sudo curl -L https://github.com/docker/compose/releases/download/1.19.0/docker-compose-`uname -s`-`uname -m` -o /usr/bin/docker-compose```<br/>
```sudo chmod +x /usr/bin/docker-compose``` <br/>
```docker-compose --version``` : should return ```docker-compose version 1.19.0, build 9e633ef```


#### Steps for Deployment

1) SSh to the target server. <br/> 
```ssh blockchain@xxx.xxx.xxx.xxx```
2) Download and unzip the repo with the commands: <br />
```wget https://github.com/GovTechSG/hashgraph_experiments/archive/master.zip```<br />
```unzip master.zip```
3) Edit the config.txt file ip address with the corresponding server's vnet ip. <br/>
Example: <br/>
 ```address,  A, Alice, 1, 127.0.0.1, 50204, 127.0.0.1, 50204``` <br/>
 ```address,  B, Bob,   1, 127.0.0.1, 50204, 127.0.0.1, 50204```
 
4) Edit the swagger url in the docker file for the parameter: ENV API_URL <br/>
```cd /home/blockchain1/hashgraph_experiments-master/supporting-services/swagger-ui-master``` <br/>
```vim Dockerfile``` <br/>
``` ENV API_URL "http://13.82.94.181:52204/swagger.json"```
 
5) Build and run the project <br />
```cd hashgraph_experiments-master/``` <br/>
```sudo docker-compose up```

<b>Sample output of docker-compose up:</b>

//TODO add the image of the output





