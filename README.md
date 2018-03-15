# Deployment in Ubuntu Server 16.04 LTS

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
 ```address,  A, Alice, 1, 10.0.0.14, 50204, 10.0.0.4, 50204``` <br/>
 ```address,  B, Bob,   1, 10.0.0.15, 50204, 10.0.0.5, 50204```
 
4) Build and run the project <br />
```cd hashgraph_experiments-master/``` <br/>
```sudo docker-compose up```

<b>Sample output of docker-compose up:</b>

//TODO add the image of the output


#### To generate keys for the nodes if needed

##### PreRequisties

<b>Java 1.8: </b> Follow the steps in the link: https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-get-on-ubuntu-16-04 to install java 8

##### Steps to generate keys
1) Generate the keys by executing the script: generate.sh <br/>
Example: <br/>
```cd /home/blockchain1/hashgraph_experiments-master/data/keys```  <br/>
```./generate.sh```
2) Copy the keys folder to another server if needed <br/>
Example:<br/>
```scp public.pfx  blockchain2@10.0.0.15://home/blockchain2/hashgraph_experiments-master/data/keys```




