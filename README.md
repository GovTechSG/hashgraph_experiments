# Deployment in Ubuntu Server 16.04 LTS

## 1. Deployment steps for multi-node cluster in same server

#### Server setup

1. Open the port 52204-52207, 8080 in the server by specifying the inbound and outbound ports to 52204 and 8080. </br>
8080 is for swagger <br/>
52204-52207 is for hashgraph nodes <br/>

##### Note: Tested in server with the below configuration
![alt text](https://github.com/GovTechSG/hashgraph_experiments/blob/master/images/azure-servers.png)


#### PreRequsites
1. <b>Unzip:</b> Make sure unzip is installed on the target server, if it is not installed, install it with <br />```sudo apt-get install unzip```<br />
2. <b>Docker (version 17.12.1-ce, build 7390fc6) </b> Make sure docker is installed on the target server, if not installed, install it with the instructions given in the below website<br />
```https://www.digitalocean.com/community/tutorials/how-to-install-and-use-docker-on-ubuntu-16-04```
###### Note: Complete Until step: 1 (Step 1 — Installing Docker) <br/>
Make sure docker is installed properly with the command ```sudo docker version```. Expected output: <br/>
![alt text](https://github.com/GovTechSG/hashgraph_experiments/blob/master/images/docker-version.png)

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
3) Edit the config.txt if required to add the nodes. <br/>
Example: <br/>
 ```address,  A, Alice, 1, 127.0.0.1, 50204, 127.0.0.1, 50204``` <br/>
 ```address,  B, Bob,   1, 127.0.0.1, 50205, 127.0.0.1, 50205```
 
4) Edit the swagger url in the docker file for the parameter: ENV API_URL <br/>
```cd hashgraph_experiments-master/supporting-services/swagger-ui-master``` <br/>
```vim Dockerfile``` <br/>
``` ENV API_URL "http://13.82.94.181:52204/swagger.json"```
 
5) Build and run the project <br />
```cd hashgraph_experiments-master/``` <br/>
```sudo docker-compose up```

<b>Expected output of docker-compose up:</b>
![alt text](https://github.com/GovTechSG/hashgraph_experiments/blob/master/images/docker-ps.png)


##### Url to access the service:

http://xxx.xxx.xxx.xxx:8080/ : This will bring up the swagger documentation for the hashgraph exposed rest endpoints.<br/>

Expected output: <br/>

![alt text](https://github.com/GovTechSG/hashgraph_experiments/blob/master/images/swagger-restendpoints.png)



## 2. Deployment steps for multi-node cluster in different servers

#### PreRequsites

1. Java 1.8 
2. Maven
3. Docker (Refer above for installation steps)
4. Docker compose (Refer above for installation steps)
5. Unzip (Refer above for installation steps)

#### Server set up

1. Set up the servers in the same vnet inorder for them to communicate with each other

2. Open the port 8080 and 52204 in the target server. 8080 for swagger and 52204 is for the hashgraph node

#### Steps to deploy

1) Download and unzip the repo with the commands: <br />
```wget https://github.com/GovTechSG/hashgraph_experiments/archive/master.zip```<br />
```unzip master.zip```

2) Edit the swagger url in the docker file for the parameter: ENV API_URL <br/>
Example: <br/>
```cd hashgraph_experiments-master/supporting-services/swagger-ui-master``` <br/>
```vim Dockerfile``` <br/>
``` ENV API_URL "http://13.82.94.181:52204/swagger.json"```
 

3) Change to hashgraph_experiments and bring up couchdb and swagger app with the command <br/>
```cd hashgraph_experiments-master```
```sudo docker-compose up```

3) Edit config.txt with the private ip of the respective servers. <br/>
Example: <br/>
 ```address,  A, Alice,    1, 10.0.0.4, 50204, 10.0.0.4, 50204``` <br/>
 ```address,  B, Bob,      1, 10.0.0.5, 50204, 10.0.0.5, 50204```

4) Bring up the hashgraph node <br/>
```./build_and_run_hashgraph_node.sh```

5) Repeat the same steps: 1 to 4 on the other server. 

##### Url to access the service:

http://xxx.xxx.xxx.xxx:8080/ : This will bring up the swagger documentation for the hashgraph exposed rest endpoints.<br/>

Expected output: <br/>

![alt text](https://github.com/GovTechSG/hashgraph_experiments/blob/master/images/swagger-restendpoints.png)

###### Notes: For the endpoints url if you see 2 entires then you can be sure that the nodes are in the cluster as expected

## LICENSE: <br/>
https://github.com/GovTechSG/hashgraph_experiments/blob/master/LICENSE.md
